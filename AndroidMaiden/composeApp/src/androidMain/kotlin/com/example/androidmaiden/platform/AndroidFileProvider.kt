package com.example.androidmaiden.platform

import android.os.Environment
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.service.FileProvider
import java.io.File
import kotlin.time.Instant

/**
 * Android implementation of [FileProvider] using java.io.File.
 */
@OptIn(kotlin.time.ExperimentalTime::class)
class AndroidFileProvider : FileProvider {
    override fun listFiles(path: String): List<FileItem> {
        val dir = File(path)
        if (!dir.exists() || !dir.isDirectory) return emptyList()

        return dir.listFiles()?.map { file ->
            FileItem(
                path = file.absolutePath,
                name = file.name,
                isDirectory = file.isDirectory,
                lastModified = file.lastModified(),
                size = if (file.isDirectory) 0L else file.length(),
                parentPath = path,
                createdAt = kotlin.time.Instant.fromEpochMilliseconds(file.lastModified())
            )
        } ?: emptyList()
    }

    override fun getRootPath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }
}
