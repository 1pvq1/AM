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

        // If timestamps match, we skip scanning the children of this directory
        if (storedTimestamp != null && storedTimestamp == currentTimestamp) {
            // We still need to recurse into subdirectories because a child
            // deep in the tree might have changed without affecting this parent's timestamp.
            // However, listFiles() is skipped for this specific level's file entries.
            scanSubDirectoriesOnly(directory)
            return
        }

        // 2. If it's new or changed, list the children
        val children = directory.listFiles() ?: return
        val metadataList = mutableListOf<FileMetadata>()

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

        // 4. Persistence: Batch upsert the children to Room。
        // Batch update the database for this specific folder
        if (metadataList.isNotEmpty()) {
            fileDao.upsertFiles(metadataList)
        }

        // 5. Update the directory's own timestamp in the DB so we can skip it next time
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