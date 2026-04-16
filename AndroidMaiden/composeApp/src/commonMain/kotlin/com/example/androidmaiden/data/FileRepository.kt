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

    /**
     * Global search across all indexed files.
     */
    fun searchFiles(query: String): Flow<List<FileMetadata>> {
        return fileDao.searchFiles(query)
    }

    /**
     * Deletes a file physically and removes its record from the database.
     */
    suspend fun deleteFile(path: String): Boolean {
        val success = scanner.deleteFile(path)
        if (success) {
            fileDao.deleteByPath(path)
        }
        return success
    }

    /**
     * Renames a file physically and updates its record in the database.
     */
    suspend fun renameFile(oldPath: String, newName: String): Boolean {
        val success = scanner.renameFile(oldPath, newName)
        if (success) {
            // Path is the primary key, so we delete old and let sync handle the new one,
            // or we could manually trigger a folder re-sync.
            fileDao.deleteByPath(oldPath)
            // Note: The new file will be picked up by the next incremental sync of its parent.
        }
        return success
    }

    fun getScannedPath(): String {
        return scanner.getScannedPath()
    }

    // --- Tag Operations ---

    fun getAllTags(): Flow<List<Tag>> = fileDao.getAllTags()

    suspend fun addTag(name: String, colorHex: String) {
        fileDao.insertTag(Tag(name = name, colorHex = colorHex))
    }

    suspend fun updateTag(tag: Tag) {
        fileDao.insertTag(tag)
    }

    suspend fun removeTag(tag: Tag) {
        fileDao.deleteTag(tag)
    }

    fun getFileWithTags(path: String): Flow<FileWithTags?> = fileDao.getFileWithTags(path)

    fun getTagWithFiles(tagId: Long): Flow<TagWithFiles?> = fileDao.getTagWithFiles(tagId)

    suspend fun addTagToFile(path: String, tagId: Long) {
        fileDao.insertFileTagXRef(FileTagXRef(path, tagId))
    }

    suspend fun removeTagFromFile(path: String, tagId: Long) {
        fileDao.deleteFileTagXRef(FileTagXRef(path, tagId))
    }
}
