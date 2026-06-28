package com.example.androidmaiden.util

import com.example.androidmaiden.data.local.FileMetadata

/**
 * Platform-independent interface for real-time file access.
 */
interface FileProvider {
    /**
     * Returns the root path for storage on the current platform.
     */
    fun getRootPath(): String

    /**
     * Lists files in a specific directory.
     * @param path The absolute path of the directory.
     * @return A list of FileMetadata for children of the directory.
     */
    suspend fun listFiles(path: String): List<FileMetadata>

    /**
     * Retrieves metadata for a single file or directory.
     * @param path The absolute path.
     * @return FileMetadata if found, null otherwise.
     */
    suspend fun getFileMetadata(path: String): FileMetadata?
}
