package com.example.androidmaiden.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileRepository
import com.example.androidmaiden.model.FileCategory
import com.example.androidmaiden.utils.FileTypeUtils
import com.example.androidmaiden.utils.FileTypeUtils.getExtensionType
import com.example.androidmaiden.utils.toFileNode
import kotlinx.coroutines.flow.*


val initialCategories =
    (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions)
        .map { def ->
            FileCategory(name = def.name, icon = def.icon, type = def.type)
        }

class PersistentFileViewModel(private val repository: FileRepository) : ViewModel() {


    // 1. Observe the Repository's sync status for UI progress indicators
    val isSyncing: StateFlow<Boolean> = repository.isSyncing

    // 2. Reactive Categories: Automatically recalculated when DB changes
    // This Flow provides the data for your "Strip Blocks" and "Classify" cards
    val categories: StateFlow<List<FileCategory>> = repository.allFiles
        .map { metadataList ->
//            if (metadataList.isEmpty()) emptyList()
//            else classifyMetadata(metadataList)

            if (metadataList.isEmpty()) {
                initialCategories // Show the framework even if DB is empty
            } else {
                processMetadata(metadataList) // Map real data when available
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
            initialValue = initialCategories // CRITICAL: Start with the framework
        )

    val currentScannedPath: StateFlow<String?> = flow {
        emit(repository.getScannedPath())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /**
     * Triggers the incremental sync via the repository.
     * Because the UI observes the Room database, results appear in real-time.
     */
    fun startSync() {
        repository.startIncrementalSync()
    }

    private fun processMetadata(list: List<FileMetadata>): List<FileCategory> {
        val groups = list.filter { !it.isDirectory }.groupBy { getExtensionType(it.fileName) }

        val classificationCategories = FileTypeUtils.categoryDefinitions.map { def ->
            val items = groups[def.type] ?: emptyList()
            FileCategory(
                name = def.name,
                icon = def.icon,
                type = def.type,
                count = items.size,
                totalSizeMb = items.sumOf { it.size } / (1024 * 1024),
                // CRITICAL: Ensure this mapping is happening! Otherwise, it will cause the detailed page to be blank.
                files = items.map { it.toFileNode() }
            )
        }

        val analysisCategories = FileTypeUtils.analysisDefinitions.map { def ->
            FileCategory(
                name = def.name,
                icon = def.icon,
                type = def.type
                // Count and size can be calculated here if needed for analysis categories
            )
        }

        return classificationCategories + analysisCategories
    }

    /**
     * Logic to group raw Database Metadata into UI-friendly Categories.
     */
    private fun classifyMetadata(list: List<FileMetadata>): List<FileCategory> {
        val groups = list.filter { !it.isDirectory }.groupBy { getExtensionType(it.fileName) }

/*        return listOf(
            mapToCategory("Images", Icons.Default.Image, "Images", groups["Images"]),
            mapToCategory("Videos", Icons.Default.Videocam, "Videos", groups["Videos"]),
            mapToCategory("Audio", Icons.Default.MusicNote, "Audio", groups["Audio"]),
            mapToCategory("Documents", Icons.Default.Description, "Documents", groups["Documents"]),
            mapToCategory("APKs", Icons.Default.Android, "APKs", groups["APKs"]),
            mapToCategory("Archives", Icons.Default.Archive, "Archives", groups["Archives"]),
            mapToCategory("Other", Icons.AutoMirrored.Filled.InsertDriveFile, "Other", groups["Other"])
        )*/

        // Map through the definitions to build the categories
        return FileTypeUtils.categoryDefinitions.map { def ->
            val items = groups[def.type] ?: emptyList()
            FileCategory(
                name = def.name,
                icon = def.icon,
                type = def.type,
                count = items.size,
                totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
            )
        }
    }

}