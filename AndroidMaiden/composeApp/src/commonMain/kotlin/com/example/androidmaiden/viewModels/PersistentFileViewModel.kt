package com.example.androidmaiden.viewModels

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileRepository
import com.example.androidmaiden.model.FileCategory
import com.example.androidmaiden.utils.FileTypeUtils
import com.example.androidmaiden.utils.FileTypeUtils.getExtensionType
import com.example.androidmaiden.views.fileSys.ViewMode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

data class StorageStats(
    val totalSize: Long = 0L,
    val fileCount: Int = 0,
    val folderCount: Int = 0
)

val initialCategories =
    (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions)
        .map { def ->
            FileCategory(name = def.name, icon = def.icon, type = def.type)
        }

@OptIn(ExperimentalTime::class)
class PersistentFileViewModel(private val repository: FileRepository) : BaseViewModel() {

    // 1. Observe the Repository's sync status for UI progress indicators
    val isSyncing: StateFlow<Boolean> = repository.isSyncing

    // 2. Storage Stats for the Header
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

    // 3. Reactive Categories
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

    // 4. Global Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

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

    val currentScannedPath: StateFlow<String?> = flow {
        emit(repository.getScannedPath())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI State
    private val _selectedCategory = MutableStateFlow<FileCategory?>(null)
    val selectedCategory: StateFlow<FileCategory?> = _selectedCategory.asStateFlow()

    private val _viewMode = MutableStateFlow(ViewMode.LIST)
    val viewMode: StateFlow<ViewMode> = _viewMode.asStateFlow()

    fun selectCategory(category: FileCategory?) {
        _selectedCategory.value = category
    }

    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Triggers the incremental sync via the repository.
     */
    fun startSync() {
        repository.startIncrementalSync()
    }

    /**
     * Deletes a file.
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