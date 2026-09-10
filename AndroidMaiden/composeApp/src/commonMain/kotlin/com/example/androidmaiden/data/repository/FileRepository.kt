package com.example.androidmaiden.data.repository

import kotlinx.coroutines.flow.*
import com.example.androidmaiden.domain.service.FileSystemScanner
import com.example.androidmaiden.domain.service.FileProvider
import com.example.androidmaiden.data.local.*
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.model.FileWithTags
import com.example.androidmaiden.domain.model.Tag
import kotlinx.coroutines.*

/**
 * Central repository for file metadata management.
 * Uses Room as the single source of truth to avoid re-scanning after restarts.
 */
class FileRepository(
    private val fileDao: FileMetadataDao,
    private val scanner: FileSystemScanner,
    private val fileProvider: FileProvider,
    private val repositoryScope: CoroutineScope
) {
    /**
     * Flow of all file metadata stored in the database.
     */
    val allFiles: Flow<List<FileItem>> = fileDao.getAllFiles()
        .map { entities -> entities.map { it.toDomain() } }
        .stateIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Flow of the total count of files in the system.
     */
    val totalFileCount: Flow<Int> = fileDao.getTotalFileCount()

    // 2. Sync Status State
    private val _isSyncing = MutableStateFlow(false)

    /**
     * Flow indicating whether a background sync is currently in progress.
     */
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
    fun getFilesByParent(path: String): Flow<List<FileItem>> {
        return fileDao.getFilesByParent(path).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Global search across all indexed files.
     */
    fun searchFiles(query: String): Flow<List<FileItem>> {
        return fileDao.searchFiles(query).map { entities ->
            entities.map { it.toDomain() }
        }
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

    /**
     * Returns the base path currently being scanned.
     */
    fun getScannedPath(): String {
        return fileProvider.getRootPath()
    }

    /**
     * Lists files from the OS in real-time and enriches them with tag data from the DB.
     * @param path The directory path to list.
     */
    fun getRealTimeFilesWithTags(path: String): Flow<List<FileWithTags>> = flow {
        // 1. Get real-time file list from the OS
        val realFiles = fileProvider.listFiles(path)
        
        // 2. Map files to FileWithTags by looking up tags in the DB for each file
        val filesWithTags = realFiles.map { file ->
            val tags = fileDao.getXRefsForFile(file.path).mapNotNull { xref ->
                fileDao.getAllTags().firstOrNull()?.find { it.id == xref.tagId }?.toDomain()
            }
            FileWithTags(file, tags)
        }
        emit(filesWithTags)
    }

    // --- Tag Operations ---

    /**
     * Returns a flow of all available tags.
     */
    fun getAllTags(): Flow<List<Tag>> = fileDao.getAllTags().map { entities ->
        entities.map { it.toDomain() }
    }

    /**
     * Adds a new tag with the given name and color.
     */
    suspend fun addTag(name: String, colorHex: String) {
        fileDao.insertTag(com.example.androidmaiden.data.local.Tag(name = name, colorHex = colorHex))
    }

    /**
     * Updates an existing tag.
     */
    suspend fun updateTag(tag: Tag) {
        fileDao.insertTag(com.example.androidmaiden.data.local.Tag.fromDomain(tag))
    }

    /**
     * Removes a tag.
     */
    suspend fun removeTag(tag: Tag) {
        fileDao.deleteTag(com.example.androidmaiden.data.local.Tag.fromDomain(tag))
    }

    /**
     * Returns a flow of a file and its associated tags.
     */
    fun getFileWithTags(path: String): Flow<FileWithTags?> = fileDao.getFileWithTags(path).map { 
        it?.toDomain()
    }

    /**
     * Returns a flow of a tag and all files associated with it.
     */
    fun getTagWithFiles(tagId: Long): Flow<List<FileItem>> = fileDao.getTagWithFiles(tagId).map { 
        it?.files?.map { entity -> entity.toDomain() } ?: emptyList()
    }

    /**
     * Associates a tag with a file.
     */
    suspend fun addTagToFile(path: String, tagId: Long) {
        fileDao.insertFileTagXRef(FileTagXRef(path, tagId))
    }

    /**
     * Removes the association between a tag and a file.
     */
    suspend fun removeTagFromFile(path: String, tagId: Long) {
        fileDao.deleteFileTagXRef(FileTagXRef(path, tagId))
    }
}
