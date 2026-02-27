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
}