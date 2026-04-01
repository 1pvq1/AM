package com.example.androidmaiden.utils


/**
 * Platform-independent interface for scanning the physical file system.
 */
interface FileSystemScanner {
    /** * Scans the storage incrementally.
     * Compares device timestamps with DB and only updates changed items.
     */
    suspend fun syncRoot()
    suspend fun stopSync()
    fun getScannedPath(): String

    /**
     * Deletes a file or directory from the physical storage.
     * @return true if successfully deleted.
     */
    suspend fun deleteFile(path: String): Boolean

    /**
     * Renames a file or directory.
     * @param oldPath The current absolute path.
     * @param newName The new name for the file/directory.
     * @return true if successfully renamed.
     */
    suspend fun renameFile(oldPath: String, newName: String): Boolean

    /**
     * Moves a file or directory to a new location.
     * @param sourcePath The current absolute path.
     * @param targetPath The new absolute path.
     * @return true if successfully moved.
     */
    suspend fun moveFile(sourcePath: String, targetPath: String): Boolean
}