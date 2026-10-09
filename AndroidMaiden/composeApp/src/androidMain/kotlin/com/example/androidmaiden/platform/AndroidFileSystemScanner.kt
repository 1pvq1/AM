package com.example.androidmaiden.platform

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.androidmaiden.core.experimental.time.TimeProvider
import com.example.androidmaiden.data.local.*
import com.example.androidmaiden.domain.service.FileSystemScanner
import java.io.File
import kotlinx.coroutines.*
import org.koin.core.context.GlobalContext

/**
 * Android implementation of the scanner using java.io.File and MediaStore ContentResolver.
 * Implements incremental scanning to skip unchanged directories and queries MediaStore
 * to guarantee non-media file detection (Documents, APKs, Archives) on real physical devices.
 */
class AndroidFileSystemScanner(
    private val fileDao: FileMetadataDao,
    private val timeProvider: TimeProvider
) : FileSystemScanner {
    val rootPath: String = Environment.getExternalStorageDirectory().absolutePath

    /**
     * Synchronizes the storage root incrementally via file system and MediaStore queries.
     */
    override suspend fun syncRoot() = withContext(Dispatchers.IO) {
        val rootFile = File(rootPath)
        if (rootFile.exists() && rootFile.isDirectory) {
            scanDirectory(rootFile)
        }

        // Query MediaStore ContentResolver as secondary pass to capture non-media files on Scoped Storage
        val context = try {
            GlobalContext.get().get<Context>()
        } catch (e: Exception) {
            null
        }
        if (context != null) {
            scanMediaStoreFiles(context)
        }
    }

    /**
     * Placeholder for stopping an active sync operation.
     */
    override suspend fun stopSync() {
        // Implementation for stopping sync if needed
    }

    /**
     * Returns the base path being scanned.
     */
    override fun getScannedPath(): String {
        return rootPath
    }

    /**
     * Deletes a file or directory from physical storage.
     */
    override suspend fun deleteFile(path: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(path)
        if (file.exists()) {
            if (file.isDirectory) {
                file.deleteRecursively()
            } else {
                file.delete()
            }
        } else {
            true
        }
    }

    /**
     * Renames a file or directory physically.
     */
    override suspend fun renameFile(oldPath: String, newName: String): Boolean = withContext(Dispatchers.IO) {
        val oldFile = File(oldPath)
        if (!oldFile.exists()) return@withContext false
        
        val parent = oldFile.parentFile ?: return@withContext false
        val newFile = File(parent, newName)
        
        if (newFile.exists()) return@withContext false
        
        oldFile.renameTo(newFile)
    }

    /**
     * Moves a file or directory to a new location.
     */
    override suspend fun moveFile(sourcePath: String, targetPath: String): Boolean = withContext(Dispatchers.IO) {
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists()) return@withContext false
        
        val targetFile = File(targetPath)
        if (targetFile.exists()) return@withContext false
        
        targetFile.parentFile?.let {
            if (!it.exists()) it.mkdirs()
        }
        
        sourceFile.renameTo(targetFile)
    }

    /**
     * Queries MediaStore.Files ContentResolver to index non-media files on physical Android devices.
     */
    private suspend fun scanMediaStoreFiles(context: Context) = withContext(Dispatchers.IO) {
        try {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.MIME_TYPE
            )

            val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            ) ?: return@withContext

            val batchList = mutableListOf<FileMetadata>()
            val dataIdx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val nameIdx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val sizeIdx = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            val dateIdx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
            val mimeIdx = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)

            cursor.use { c ->
                while (c.moveToNext()) {
                    val filePath = if (dataIdx >= 0) c.getString(dataIdx) else null
                    if (filePath.isNullOrBlank()) continue
                    val fileObj = File(filePath)
                    if (fileObj.isDirectory) continue

                    val name = if (nameIdx >= 0) c.getString(nameIdx) else fileObj.name
                    val size = if (sizeIdx >= 0) c.getLong(sizeIdx) else fileObj.length()
                    val dateModifiedSec = if (dateIdx >= 0) c.getLong(dateIdx) else 0L
                    val lastModified = if (dateModifiedSec > 0) dateModifiedSec * 1000L else fileObj.lastModified()
                    val mimeType = if (mimeIdx >= 0) c.getString(mimeIdx) else null

                    val metadata = FileMetadata(
                        path = filePath,
                        name = name ?: fileObj.name,
                        isDirectory = false,
                        lastModified = lastModified,
                        size = size,
                        parentPath = fileObj.parent ?: rootPath,
                        mimeType = mimeType
                    )

                    val finalMetadata = extractMediaMetadata(fileObj, metadata)
                    batchList.add(finalMetadata)

                    if (batchList.size >= 250) {
                        fileDao.upsertFiles(batchList.toList())
                        batchList.clear()
                    }
                }
            }

            if (batchList.isNotEmpty()) {
                fileDao.upsertFiles(batchList)
            }
        } catch (e: Exception) {
            // Suppress or log MediaStore query issues gracefully
        }
    }

    /**
     * Recursively scans a directory, skipping unchanged ones.
     */
    private suspend fun scanDirectory(directory: File) {
        val path = directory.absolutePath
        val currentTimestamp = directory.lastModified()

        val storedTimestamp = fileDao.getStoredTimestamp(path)

        if (storedTimestamp != null && storedTimestamp == currentTimestamp) {
            if (!fileDao.hasPendingMetadata(path)) {
                cleanupDeletedFiles(directory)
                scanSubDirectoriesOnly(directory)
                return
            }
        }

        val children = directory.listFiles() ?: return
        val metadataList = mutableListOf<FileMetadata>()
        
        val storedPaths = fileDao.getPathsByParent(path)
        val currentChildPaths = children.map { it.absolutePath }.toSet()
        val deletedPaths = storedPaths.filter { it !in currentChildPaths }
        if (deletedPaths.isNotEmpty()) {
            fileDao.deleteByPaths(deletedPaths)
        }

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
                val finalMetadata = extractMediaMetadata(child, baseMetadata)
                metadataList.add(finalMetadata)
            }
        }

        if (metadataList.isNotEmpty()) {
            fileDao.upsertFiles(metadataList)
        }

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

        for (subDir in subDirs) {
            scanDirectory(subDir)
        }
    }

    /**
     * Extracts rich media metadata using MediaMetadataRetriever.
     */
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
     * Checks files in the DB for a directory that hasn't changed its timestamp.
     */
    private suspend fun cleanupDeletedFiles(directory: File) {
        val path = directory.absolutePath
        val storedPaths = fileDao.getPathsByParent(path)

        val deletedPaths = storedPaths.filter { !File(it).exists() }
        if (deletedPaths.isNotEmpty()) {
            fileDao.deleteByPaths(deletedPaths)
        }
    }

    /**
     * Efficiently skips file entries and only dives into subdirectories.
     */
    private suspend fun scanSubDirectoriesOnly(directory: File) {
        val subDirs = directory.listFiles { file -> file.isDirectory } ?: return
        for (dir in subDirs) {
            scanDirectory(dir)
        }
    }
}
