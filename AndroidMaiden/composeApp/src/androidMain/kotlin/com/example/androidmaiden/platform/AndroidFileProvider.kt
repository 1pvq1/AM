package com.example.androidmaiden.platform

import android.os.Environment
import android.os.StatFs
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.model.StorageLocationInfo
import com.example.androidmaiden.domain.service.FileProvider
import java.io.File
import kotlin.time.Instant

/**
 * Android implementation of [FileProvider] using java.io.File and StatFs.
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

    override fun getStorageLocationInfo(): StorageLocationInfo {
        val root = Environment.getExternalStorageDirectory()
        val path = root.absolutePath
        val isRemovable = Environment.isExternalStorageRemovable(root)
        val isEmulated = Environment.isExternalStorageEmulated(root)

        val name = when {
            isRemovable -> "External SD Card Storage"
            isEmulated -> "Primary Internal Storage"
            else -> "Internal Storage"
        }

        return try {
            val stat = StatFs(path)
            val total = stat.totalBytes
            val free = stat.availableBytes
            val used = (total - free).coerceAtLeast(0L)

            StorageLocationInfo(
                name = name,
                path = path,
                isExternalRemovable = isRemovable,
                totalBytes = total,
                usedBytes = used,
                freeBytes = free
            )
        } catch (e: Exception) {
            StorageLocationInfo(
                name = name,
                path = path,
                isExternalRemovable = isRemovable
            )
        }
    }
}
