package com.example.androidmaiden.domain.service

import com.example.androidmaiden.domain.model.FileItem

/**
 * Interface for providing file listing and root path information.
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
}
