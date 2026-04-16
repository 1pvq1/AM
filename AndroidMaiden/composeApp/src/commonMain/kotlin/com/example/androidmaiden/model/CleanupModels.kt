package com.example.androidmaiden.model

import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.TrashEntry

sealed class CleanupResult {
    object Success : CleanupResult()
    data class Error(val message: String) : CleanupResult()
    data class Loading(val progress: Float? = null) : CleanupResult()
}

data class CleanupStats(
    val duplicateFiles: List<FileMetadata> = emptyList(),
    val emptyFolders: List<FileMetadata> = emptyList(),
    val uninstalledRemnants: List<FileMetadata> = emptyList(),
    val appCacheFiles: List<FileMetadata> = emptyList(),
    val trashEntries: List<TrashEntry> = emptyList()
) {
    val totalRedundantSize: Long get() = (duplicateFiles.sumOf { it.size } + 
                                       uninstalledRemnants.sumOf { it.size } + 
                                       appCacheFiles.sumOf { it.size })
    
    val totalTrashSize: Long get() = trashEntries.sumOf { it.size }
}

enum class CleanupType {
    DUPLICATES,
    EMPTY_FOLDERS,
    REMNANTS,
    CACHE,
    TRASH
}
