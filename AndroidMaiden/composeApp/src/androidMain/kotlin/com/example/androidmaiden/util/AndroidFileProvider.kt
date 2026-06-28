package com.example.androidmaiden.util

import android.os.Environment
import com.example.androidmaiden.data.local.FileMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Android implementation of FileProvider using java.io.File.
 */
class AndroidFileProvider : FileProvider {
    
    override fun getRootPath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    override suspend fun listFiles(path: String): List<FileMetadata> = withContext(Dispatchers.IO) {
        val directory = File(path)
        if (!directory.exists() || !directory.isDirectory) return@withContext emptyList()
        
        directory.listFiles()?.map { it.toMetadata() } ?: emptyList()
    }

    override suspend fun getFileMetadata(path: String): FileMetadata? = withContext(Dispatchers.IO) {
        val file = File(path)
        if (!file.exists()) return@withContext null
        
        file.toMetadata()
    }

    /**
     * Extension function to convert java.io.File to FileMetadata.
     */
    private fun File.toMetadata(): FileMetadata {
        return FileMetadata(
            path = absolutePath,
            name = name,
            isDirectory = isDirectory,
            lastModified = lastModified(),
            size = if (isDirectory) 0L else length(),
            parentPath = parent ?: ""
        )
    }
}
