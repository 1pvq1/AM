package com.example.androidmaiden.domain.model

sealed class CleanupResult {
    object Success : CleanupResult()
    data class Error(val message: String) : CleanupResult()
    data class Loading(val progress: Float? = null) : CleanupResult()
}

/**
 * Domain-level representation of a trash entry.
 */
data class TrashRecord(
    val originalPath: String,
    val trashPath: String,
    val deletedAt: Long,
    val fileName: String,
    val size: Long,
    val isDirectory: Boolean
)

data class CleanupStats(
    val duplicateFiles: List<FileItem> = emptyList(),
    val emptyFolders: List<FileItem> = emptyList(),
    val uninstalledRemnants: List<FileItem> = emptyList(),
    val appCacheFiles: List<FileItem> = emptyList(),
    val trashEntries: List<TrashRecord> = emptyList()
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
