package com.example.androidmaiden.data.repository

import kotlinx.coroutines.flow.*
import com.example.androidmaiden.util.FileSystemScanner
import com.example.androidmaiden.util.FileProvider
import com.example.androidmaiden.data.local.*
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
    val allFiles: Flow<List<FileMetadata>> = fileDao.getAllFiles()
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
        // Since we want this to be reactive to tag changes, we combine it with tag xref flows
        // For simplicity and performance in a list, we'll fetch the current tags for these paths
        val filesWithTags = realFiles.map { file ->
            val tags = fileDao.getXRefsForFile(file.path).mapNotNull { xref ->
                // This is a bit heavy, in a real app we might want a more efficient join
                // but for now it satisfies the requirement of combining OS and DB.
                fileDao.getAllTags().firstOrNull()?.find { it.id == xref.tagId }
            }
            FileWithTags(file, tags)
        }
        emit(filesWithTags)
    }

    // --- Tag Operations ---

    /**
     * Returns a flow of all available tags.
     */
    fun getAllTags(): Flow<List<Tag>> = fileDao.getAllTags()

    /**
     * Adds a new tag with the given name and color.
     */
    suspend fun addTag(name: String, colorHex: String) {
        fileDao.insertTag(Tag(name = name, colorHex = colorHex))
    }

    /**
     * Updates an existing tag.
     */
    suspend fun updateTag(tag: Tag) {
        fileDao.insertTag(tag)
    }

    /**
     * Removes a tag.
     */
    suspend fun removeTag(tag: Tag) {
        fileDao.deleteTag(tag)
    }

    /**
     * Returns a flow of a file and its associated tags.
     */
    fun getFileWithTags(path: String): Flow<FileWithTags?> = fileDao.getFileWithTags(path)

    /**
     * Returns a flow of a tag and all files associated with it.
     */
    fun getTagWithFiles(tagId: Long): Flow<TagWithFiles?> = fileDao.getTagWithFiles(tagId)

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
