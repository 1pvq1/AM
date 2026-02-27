package com.example.androidmaiden.mods

import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileMetadataDao
import java.io.File

class FileSyncManager(private val dao: FileMetadataDao) {

    suspend fun syncDirectory(directory: File) {
        if (!directory.exists() || !directory.isDirectory) return

        val realTimestamp = directory.lastModified()
        val storedTimestamp = dao.getStoredTimestamp(directory.absolutePath)

        // Optimization: If timestamps match, this folder's direct children haven't changed
        if (realTimestamp == storedTimestamp) {
            // We still need to check subdirectories because changes deep inside
            // don't always update the parent folder's timestamp on Android.
            directory.listFiles()?.filter { it.isDirectory }?.forEach { syncDirectory(it) }
            return
        }

        // Folder has changed, refresh direct children
        val children = directory.listFiles() ?: return
        val metadataList = children.map { file ->
            FileMetadata(
                path = file.absolutePath,
                fileName = file.name,
                isDirectory = file.isDirectory,
                lastModified = file.lastModified(),
                size = if (file.isDirectory) 0L else file.length(),
                parentPath = directory.absolutePath
            )
        }

        dao.upsertFiles(metadataList)

        // Recursively sync subdirectories
        children.filter { it.isDirectory }.forEach { syncDirectory(it) }
    }
}
