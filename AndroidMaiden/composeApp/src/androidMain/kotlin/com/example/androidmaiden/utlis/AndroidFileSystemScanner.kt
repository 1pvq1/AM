package com.example.androidmaiden.utils

import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Environment
import com.example.androidmaiden.data.*
import java.io.File
import kotlinx.coroutines.*

/**
 * Android implementation of the scanner using java.io.File.
 * Implements incremental scanning to skip unchanged directories.
 * Enhanced to extract media metadata for Videos, Audio, and Images.
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
        // Implementation for stopping sync if needed
    }

    override fun getScannedPath(): String {
        return rootPath
    }

    private suspend fun scanDirectory(directory: File) {
        val path = directory.absolutePath
        val currentTimestamp = directory.lastModified()

        // 1. Check if the folder has changed since the last scan
        val storedTimestamp = fileDao.getStoredTimestamp(path)

        // If timestamp matches AND no pending metadata, skip deep scan of this folder's children
        if (storedTimestamp != null && storedTimestamp == currentTimestamp) {
            if (!fileDao.hasPendingMetadata(path)) {
                cleanupDeletedFiles(directory)
                scanSubDirectoriesOnly(directory)
                return
            }
        }

        // 2. Directory changed or has pending metadata: scan all children
        val children = directory.listFiles() ?: return
        val metadataList = mutableListOf<FileMetadata>()
        
        // --- DELETION SYNC ---
        val storedPaths = fileDao.getPathsByParent(path)
        val currentChildPaths = children.map { it.absolutePath }.toSet()
        val deletedPaths = storedPaths.filter { it !in currentChildPaths }
        if (deletedPaths.isNotEmpty()) {
            fileDao.deleteByPaths(deletedPaths)
        }
        // ---------------------

        val subDirs = mutableListOf<File>()

        for (child in children) {
            val baseMetadata = FileMetadata(
                path = child.absolutePath,
                name = child.name,
                isDirectory = child.isDirectory,
                lastModified = child.lastModified(),
                size = if (child.isDirectory) 0L else child.length(),
                parentPath = path
            )

            if (child.isDirectory) {
                metadataList.add(baseMetadata)
                subDirs.add(child)
            } else {
                // Extract rich metadata for files
                val finalMetadata = extractMediaMetadata(child, baseMetadata)
                metadataList.add(finalMetadata)
            }
        }

        // 3. Persistence: Batch upsert the children of CURRENT directory immediately
        // This allows the UI to update as we progress through folders.
        if (metadataList.isNotEmpty()) {
            fileDao.upsertFiles(metadataList)
        }

        // 4. Update the directory's own timestamp in the DB
        fileDao.upsertFiles(listOf(
            FileMetadata(
                path = path,
                name = directory.name,
                isDirectory = true,
                lastModified = currentTimestamp,
                size = 0L,
                parentPath = directory.parent ?: ""
            )
        ))

        // 5. Recurse into subdirectories AFTER saving current directory progress
        for (subDir in subDirs) {
            scanDirectory(subDir)
        }
    }

    private fun extractMediaMetadata(file: File, base: FileMetadata): FileMetadata {
        val extension = file.extension.lowercase()
        val isVideo = extension in listOf("mp4", "mkv", "mov", "avi", "wmv", "webm")
        val isAudio = extension in listOf("mp3", "wav", "flac", "aac", "m4a", "ogg")
        val isImage = extension in listOf("jpg", "jpeg", "png", "webp", "gif", "bmp", "heic")

        if (!isVideo && !isAudio && !isImage) return base

        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)

            var duration: Long? = null
            var width: Int? = null
            var height: Int? = null
            var artist: String? = null
            var album: String? = null
            var bitrate: Long? = null

            if (isVideo || isAudio) {
                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLongOrNull()
            }

            if (isVideo) {
                width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull()
                height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull()
            } else if (isImage && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Image resolution keys require API 28+
                width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_WIDTH)?.toIntOrNull()
                height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_HEIGHT)?.toIntOrNull()
            }

            if (isAudio) {
                artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            }

            base.copy(
                duration = duration,
                width = width,
                height = height,
                artist = artist,
                album = album,
                bitrate = bitrate,
                metadataStatus = 1
            )
        } catch (e: Exception) {
            base.copy(metadataStatus = 2)
        } finally {
            retriever.release()
        }
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
