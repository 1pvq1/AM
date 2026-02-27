package com.example.androidmaiden.data

import kotlinx.coroutines.flow.*
import com.example.androidmaiden.utils.FileSystemScanner
import kotlinx.coroutines.*

/**
 * Central repository for file metadata management.
 * Uses Room as the single source of truth to avoid re-scanning after restarts.
 */
class FileRepository(
    private val fileDao: FileMetadataDao,
    private val scanner: FileSystemScanner,
    private val repositoryScope: CoroutineScope
) {
    // 1. Reactive Data Streams: UI observes these directly
    val allFiles: Flow<List<FileMetadata>> = fileDao.getAllFiles()
        .stateIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val totalFileCount: Flow<Int> = fileDao.getTotalFileCount()

    // 2. Sync Status State
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    /**
     * Triggers the platform-specific incremental sync.
     * Updates the database, which automatically refreshes all observing UIs.
     */
    fun startIncrementalSync() {
        if (_isSyncing.value) return

        repositoryScope.launch(Dispatchers.Default) {
            _isSyncing.value = true
            try {
                scanner.syncRoot()
            } catch (e: Exception) {
                // Handle or log synchronization errors
            } finally {
                _isSyncing.value = false
            }
        }
    }

    /**
     * Targeted query for specific categories used in ViewModels.
     */
    fun getFilesByParent(path: String): Flow<List<FileMetadata>> {
        return fileDao.getFilesByParent(path)
    }

    suspend fun deleteFileRecord(path: String) {
        fileDao.deleteByPath(path)
    }

    fun getScannedPath(): String {
        return scanner.getScannedPath()
    }
}