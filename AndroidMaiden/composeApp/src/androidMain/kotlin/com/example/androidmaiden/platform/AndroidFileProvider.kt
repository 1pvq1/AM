package com.example.androidmaiden.platform

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.core.content.ContextCompat
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.model.StorageLocationInfo
import com.example.androidmaiden.domain.service.FileProvider
import java.io.File
import kotlin.time.Instant
import org.koin.core.context.GlobalContext

/**
 * Android implementation of [FileProvider] using java.io.File, StatFs, and Context storage API.
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
        val isRemovable = Environment.isExternalStorageRemovable(root)
        val isEmulated = Environment.isExternalStorageEmulated(root)

        val name = when {
            isRemovable -> "External SD Card Storage"
            isEmulated -> "Primary Internal Storage"
            else -> "Internal Storage"
        }

        return createLocationInfo(name, root, isRemovable)
    }

    override fun getStorageLocations(): List<StorageLocationInfo> {
        val locations = mutableListOf<StorageLocationInfo>()
        val primaryInfo = getStorageLocationInfo()
        locations.add(primaryInfo)

        val context = try {
            GlobalContext.get().get<Context>()
        } catch (e: Exception) {
            null
        }

        val seenPaths = mutableSetOf(primaryInfo.path)

        if (context != null) {
            try {
                val externalDirs = ContextCompat.getExternalFilesDirs(context, null)
                for (dir in externalDirs) {
                    if (dir != null) {
                        val volumeRoot = getVolumeRoot(dir)
                        if (volumeRoot != null && volumeRoot.absolutePath !in seenPaths && volumeRoot.exists() && volumeRoot.canRead()) {
                            seenPaths.add(volumeRoot.absolutePath)
                            val isRemovable = Environment.isExternalStorageRemovable(volumeRoot)
                            val name = if (isRemovable) "External SD Card Storage" else "Secondary Storage"
                            locations.add(createLocationInfo(name, volumeRoot, isRemovable))
                        }
                    }
                }
            } catch (e: Exception) {
                // Suppress storage enumeration errors gracefully
            }
        }

        // Fallback pass: Scan /storage directory for mounted external storage volumes
        try {
            val storageDir = File("/storage")
            if (storageDir.exists() && storageDir.isDirectory) {
                val subFiles = storageDir.listFiles() ?: emptyArray()
                for (file in subFiles) {
                    val absPath = file.absolutePath
                    if (absPath !in seenPaths && file.isDirectory && file.canRead() &&
                        absPath != "/storage/emulated" && absPath != "/storage/self"
                    ) {
                        seenPaths.add(absPath)
                        val isRemovable = Environment.isExternalStorageRemovable(file)
                        val name = if (isRemovable) "External SD Card Storage" else "External Storage"
                        locations.add(createLocationInfo(name, file, isRemovable))
                    }
                }
            }
        } catch (e: Exception) {
            // Suppress directory scanning errors gracefully
        }

        return locations
    }

    /**
     * Traverses up a file hierarchy to extract the volume root under /storage.
     */
    private fun getVolumeRoot(file: File): File? {
        var current: File? = file
        while (current != null) {
            val parent = current.parentFile
            if (parent != null && parent.absolutePath == "/storage") {
                return current
            }
            current = parent
        }
        return null
    }

    /**
     * Creates a [StorageLocationInfo] model for a given directory file.
     */
    private fun createLocationInfo(name: String, file: File, isRemovable: Boolean): StorageLocationInfo {
        val path = file.absolutePath
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
