package com.example.androidmaiden.domain.service

import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.model.StorageLocationInfo

/**
 * Interface for providing file listing, root path, and storage volume metrics.
 */
interface FileProvider {
    /**
     * Lists all files and folders in the given [path].
     */
    fun listFiles(path: String): List<FileItem>

    /**
     * Returns the root path of the file system.
     */
    fun getRootPath(): String

    /**
     * Returns detected storage location metrics (Internal vs External SD Card).
     */
    fun getStorageLocationInfo(): StorageLocationInfo
}
