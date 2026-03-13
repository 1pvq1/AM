package com.example.androidmaiden.utils

import android.os.Environment
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileMetadataDao
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of the scanner using java.io.File.
 * Implements incremental scanning to skip unchanged directories.
 */
class AndroidFileSystemScanner(
    private val fileDao: FileMetadataDao
) : FileSystemScanner {
    val rootPath: String = Environment.getExternalStorageDirectory().absolutePath

    override suspend fun syncRoot() = withContext(Dispatchers.IO) {
        // Start at the primary external storage root (/storage/emulated/0)
        val rootFile = File(rootPath)

        if (rootFile.exists() && rootFile.isDirectory) {
            scanDirectory(rootFile)
        }
    }

    override suspend fun stopSync() {
        TODO("Not yet implemented")
    }

    override fun getScannedPath(): String {
        return rootPath
    }

    private suspend fun scanDirectory(directory: File) {
        val path = directory.absolutePath
        val currentTimestamp = directory.lastModified()

        // 1. Check if the folder has changed since the last scan
        val storedTimestamp = fileDao.getStoredTimestamp(path)

        // Even if timestamps match, we MUST check for deletions in this directory.
        // Some file operations (especially external ones) don't always update the parent folder's timestamp.
        if (storedTimestamp != null && storedTimestamp == currentTimestamp) {
            cleanupDeletedFiles(directory)
            scanSubDirectoriesOnly(directory)
            return
        }

        // 2. If it's new or changed, list the children
        val children = directory.listFiles() ?: return
        val metadataList = mutableListOf<FileMetadata>()
        
        // --- DELETION SYNC ---
        // Get all paths currently stored in DB for this directory
        val storedPaths = fileDao.getPathsByParent(path)
        val currentChildPaths = children.map { it.absolutePath }.toSet()
        
        // Identify paths in DB that are no longer on disk
        val deletedPaths = storedPaths.filter { it !in currentChildPaths }
        if (deletedPaths.isNotEmpty()) {
            fileDao.deleteByPaths(deletedPaths)
        }
        // ---------------------

        for (child in children) {
            metadataList.add(
                FileMetadata(
                    path = child.absolutePath,
                    fileName = child.name,
                    isDirectory = child.isDirectory,
                    lastModified = child.lastModified(),
                    size = if (child.isDirectory) 0L else child.length(),
                    parentPath = path
                )
            )

            // 3. Recurse into subdirectories
            if (child.isDirectory) {
                scanDirectory(child)
            }
        }

        // 4. Persistence: Batch upsert the children to Room
        if (metadataList.isNotEmpty()) {
            fileDao.upsertFiles(metadataList)
        }

        // 5. Update the directory's own timestamp in the DB
        fileDao.upsertFiles(listOf(
            FileMetadata(
                path = path,
                fileName = directory.name,
                isDirectory = true,
                lastModified = currentTimestamp,
                size = 0L,
                parentPath = directory.parent ?: ""
            )
        ))
    }

    /**
     * Checks files in the DB for a directory that hasn't changed its timestamp,
     * ensuring that files deleted externally are removed from the app's database.
     */
    private suspend fun cleanupDeletedFiles(directory: File) {
        val path = directory.absolutePath
        val storedPaths = fileDao.getPathsByParent(path)
        
        // Filter for paths that no longer exist physically
        val deletedPaths = storedPaths.filter { !File(it).exists() }
        
        if (deletedPaths.isNotEmpty()) {
            fileDao.deleteByPaths(deletedPaths)
        }
    }

    /**
     * Efficiently skips file entries and only dives into folders
     * when the parent directory timestamp matches our cache.
     */
    private suspend fun scanSubDirectoriesOnly(directory: File) {
        val subDirs = directory.listFiles { file -> file.isDirectory } ?: return
        for (dir in subDirs) {
            scanDirectory(dir)
        }
    }
}