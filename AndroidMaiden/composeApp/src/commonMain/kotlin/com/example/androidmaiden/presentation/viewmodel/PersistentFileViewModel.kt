package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.local.FileMetadata
import com.example.androidmaiden.data.repository.FileRepository
import com.example.androidmaiden.domain.model.FileCategory
import com.example.androidmaiden.util.FileTypeUtils
import com.example.androidmaiden.util.FileTypeUtils.getExtensionType
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Data class for basic storage usage statistics.
 */
data class StorageStats(
    val totalSize: Long = 0L,
    val fileCount: Int = 0,
    val folderCount: Int = 0
)

/**
 * Initial empty categories based on definitions.
 */
val initialCategories =
    (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions)
        .map { def ->
            FileCategory(name = def.name, icon = def.icon, type = def.type)
        }

/**
 * ViewModel for persistent file classification and management.
 */
@OptIn(ExperimentalTime::class)
class PersistentFileViewModel(private val repository: FileRepository) : BaseViewModel() {

    /**
     * Flow of the repository's sync status.
     */
    val isSyncing: StateFlow<Boolean> = repository.isSyncing

    /**
     * Flow of calculated storage statistics.
     */
    val storageStats: StateFlow<StorageStats> = repository.allFiles
        .map { list ->
            StorageStats(
                totalSize = list.sumOf { it.size },
                fileCount = list.count { !it.isDirectory },
                folderCount = list.count { it.isDirectory }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StorageStats()
        )

    /**
     * Flow of processed file categories with file lists and counts.
     */
    val categories: StateFlow<List<FileCategory>> = repository.allFiles
        .map { metadataList ->
            if (metadataList.isEmpty()) {
                initialCategories 
            } else {
                processMetadata(metadataList)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialCategories 
        )

    private val _searchQuery = MutableStateFlow("")

    /**
     * Current search query string.
     */
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /**
     * Flow of search results from the repository.
     */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<FileMetadata>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length < 2) flowOf(emptyList())
            else repository.searchFiles(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Flow of the current base path being scanned.
     */
    val currentScannedPath: StateFlow<String?> = flow {
        emit(repository.getScannedPath())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedCategory = MutableStateFlow<FileCategory?>(null)

    /**
     * The currently selected file category for detailed view.
     */
    val selectedCategory: StateFlow<FileCategory?> = _selectedCategory.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.LIST)

    /**
     * The current view mode (List or Grid).
     */
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

    /**
     * Selects a category to display.
     */
    fun selectCategory(category: FileCategory?) {
        _selectedCategory.value = category
    }

    /**
     * Sets the UI view mode.
     */
    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    /**
     * Updates the global search query.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Triggers an incremental sync via the repository.
     */
    fun startSync() {
        repository.startIncrementalSync()
    }

    /**
     * Deletes a file by its path.
     */
    fun deleteFile(path: String) {
        viewModelScope.launch {
            repository.deleteFile(path)
        }
    }

    /**
     * Renames a file.
     */
    fun renameFile(oldPath: String, newName: String) {
        viewModelScope.launch {
            repository.renameFile(oldPath, newName)
        }
    }

    /**
     * Processes raw metadata into categorized lists.
     */
    private fun processMetadata(list: List<FileMetadata>): List<FileCategory> {
        val allFilesOnly = list.filter { !it.isDirectory }
        val groups = allFilesOnly.groupBy { getExtensionType(it.name) }

        val classificationCategories = FileTypeUtils.categoryDefinitions.map { def ->
            val items = groups[def.type] ?: emptyList()
            FileCategory(
                name = def.name,
                icon = def.icon,
                type = def.type,
                count = items.size,
                totalSizeMb = items.sumOf { it.size } / (1024 * 1024),
                files = items
            )
        }

        val nowMillis = Clock.System.now().toEpochMilliseconds()
        val sevenDaysAgo = nowMillis - 7.days.inWholeMilliseconds
        val largeFileThreshold = 50 * 1024 * 1024L

        val analysisCategories = FileTypeUtils.analysisDefinitions.map { def ->
            val filteredFiles = when (def.type) {
                "LargeFiles" -> allFilesOnly.filter { it.size > largeFileThreshold }
                "RecentFiles" -> allFilesOnly.filter { it.lastModified > sevenDaysAgo }
                "Other" -> groups["Other"] ?: emptyList()
                else -> emptyList()
            }

            FileCategory(
                name = def.name,
                icon = def.icon,
                type = def.type,
                count = filteredFiles.size,
                totalSizeMb = filteredFiles.sumOf { it.size } / (1024 * 1024),
                files = filteredFiles
            )
        }

        return classificationCategories + analysisCategories
    }
}
