package com.example.androidmaiden.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Centrally managed file categories to be shared across the app.
 */
object FileTypeUtils {
    fun getExtensionType(fileName: String): String {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        return when (ext) {
            // Images
            "jpg", "jpeg", "png", "webp", "gif", "bmp", "heic" -> "Images"

            // Videos
            "mp4", "mkv", "mov", "avi", "wmv", "webm" -> "Videos"

            // Audio
            "mp3", "wav", "flac", "aac", "m4a", "ogg" -> "Audio"

            // Documents
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "md" -> "Documents"

            // Application packages
            "apk", "aab" -> "APKs"

            // Archives
            "zip", "rar", "7z", "tar", "gz" -> "Archives"
            else -> "Other"
        }
    }

    // 1. Central source of truth for categories
    val categoryDefinitions = listOf(
        CategoryDef("Images", Icons.Default.Image, "Images"),
        CategoryDef("Videos", Icons.Default.Videocam, "Videos"),
        CategoryDef("Audio", Icons.Default.MusicNote, "Audio"),
        CategoryDef("Documents", Icons.Default.Description, "Documents"),
        CategoryDef("APKs", Icons.Default.Android, "APKs"),
        CategoryDef("Archives", Icons.Default.Archive, "Archives"),
        )

    // 2. Specialized categories for analysis (Non-extension based)
    val analysisDefinitions = listOf(
        CategoryDef("Large Files (> 50MB)", Icons.Default.DiscFull, "LargeFiles"),
        CategoryDef("Recent Files (Last 7 Days)", Icons.Default.Schedule, "RecentFiles"),
        CategoryDef("Other", Icons.AutoMirrored.Filled.InsertDriveFile, "Other")
    )

}

/**
which performs a "flatten" operation on the tree. In a mature file manager, this is replaced by
SQL queries or List filtering on the database records, which is significantly faster and survives
app restarts.
Thread Safety
 */
suspend fun calculateCategoryDetails(
    root: FileSysNode,
    baseCategories: List<FileCategory>
): List<FileCategory> = withContext(Dispatchers.Default) {
    val files = root.flatten().filter { !it.isFolder }
    val counts = root.countByType()
    val sizes = root.totalSizeByType()

    val updated = baseCategories.map { category ->
        when (category.type) {
            "LargeFiles" -> {
                val largeFiles = root.getLargeFiles(thresholdMb = 50)
                val sizeMb = largeFiles.sumOf { it.size ?: 0L } / (1024 * 1024)
                category.copy(count = largeFiles.size, totalSizeMb = sizeMb, files = largeFiles)
            }

            "RecentFiles" -> {
                val recentFiles = root.getRecentFiles(withinMillis = 7 * 24 * 60 * 60 * 1000)
                val sizeMb = recentFiles.sumOf { it.size ?: 0L } / (1024 * 1024)
                category.copy(
                    count = recentFiles.size,
                    totalSizeMb = sizeMb,
                    files = recentFiles
                )
            }

            else -> { // Standard file types including "Other"
                val filesForCategory = files.filter { it.extensionGroup() == category.type }
                val count = counts[category.type] ?: 0
                val sizeMb = (sizes[category.type] ?: 0L) / (1024 * 1024)
                category.copy(count = count, totalSizeMb = sizeMb, files = filesForCategory)
            }
        }
    }
    updated // No longer sorting to preserve section order
}


fun FileMetadata.toFileNode(): FileSysNode {
    return FileSysNode(
        name = this.fileName,
        path = this.path,
        nodeType = if (this.isDirectory) NodeType.FOLDER else NodeType.FILE,
        size = this.size,
        lastModified = this.lastModified,
        children = emptyList() // Metadata records are flat, not trees
    )
}

private fun mapToCategory(
    def: CategoryDef,
    files: List<FileMetadata>?
): FileCategory {
    val items = files ?: emptyList()
    return FileCategory(
        name = def.name,
        icon = def.icon,
        type = def.type,
        count = items.size,
        totalSizeMb = items.sumOf { it.size } / (1024 * 1024),
        // Note: If your FileCategory UI still requires FileNode,
        // you can map FileMetadata to FileNode here.

        // CRITICAL: Map to the UI model expected by FileListPage
        files = items.map { it.toFileNode() }
    )
}