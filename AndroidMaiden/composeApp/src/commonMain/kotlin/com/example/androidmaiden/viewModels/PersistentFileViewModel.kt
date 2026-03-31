package com.example.androidmaiden.viewModels

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileRepository
import com.example.androidmaiden.model.FileCategory
import com.example.androidmaiden.utils.FileTypeUtils
import com.example.androidmaiden.utils.FileTypeUtils.getExtensionType
import com.example.androidmaiden.views.fileSys.ViewMode
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime


val initialCategories =
    (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions)
        .map { def ->
            FileCategory(name = def.name, icon = def.icon, type = def.type)
        }

@ExperimentalTime
class PersistentFileViewModel(private val repository: FileRepository) : BaseViewModel() {


    // 1. Observe the Repository's sync status for UI progress indicators
    val isSyncing: StateFlow<Boolean> = repository.isSyncing

    // 2. Reactive Categories: Automatically recalculated when DB changes
    // This Flow provides the data for your "Strip Blocks" and "Classify" cards
    val categories: StateFlow<List<FileCategory>> = repository.allFiles
        .map { metadataList ->
            if (metadataList.isEmpty()) {
                initialCategories // Show the framework even if DB is empty
            } else {
                processMetadata(metadataList) // Map real data when available
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialCategories // CRITICAL: Start with the framework
        )

    val currentScannedPath: StateFlow<String?> = flow {
        emit(repository.getScannedPath())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI State that survives configuration changes (rotation, split-screen)
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

    /**
     * Triggers the incremental sync via the repository.
     * Because the UI observes the Room database, results appear in real-time.
     */
    fun startSync() {
        repository.startIncrementalSync()
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

        val nowMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
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