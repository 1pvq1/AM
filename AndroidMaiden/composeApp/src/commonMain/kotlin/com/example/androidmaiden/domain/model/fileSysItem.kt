//@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Domain-level representation of a file or folder.
 * Free from any database or network annotations.
 */
@OptIn(ExperimentalTime::class)
data class FileItem (
//    data class FileItem @OptIn(ExperimentalTime::class) constructor(
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val lastModified: Long,
    val size: Long,
    val parentPath: String,
    val mimeType: String? = null,
    val duration: Long? = null,
    val artist: String? = null,
    val album: String? = null,
    val bitrate: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val excerptPath: String? = null,
    val metadataStatus: Int = 0,
    val isFavorite: Boolean = false,
    val isTrash: Boolean = false,
//    val createdAt: kotlin.time.Instant? = null
    val createdAt: Instant? = null
) {
    val extension: String get() = name.substringAfterLast('.', "").lowercase()
    val isFile: Boolean get() = !isDirectory
}

// Folder categories that display specific folder icons to distinguish them from the default folder icons
enum class FolderType {
    FOLDER, DOCUMENT, IMAGE, MUSIC, VIDEO, OTHER
}

enum class NodeType { FILE, FOLDER }

/**
 * Experimental feature intended only for specific pages (analysis page),
 * and should not currently be used elsewhere.
 * */
enum class DataSource { MOCK, REAL }

/**
 * Core data structure for representing a file or folder.
 *
 * Experimental feature intended only for specific pages (analysis page),
 * and should not currently be used elsewhere.
 * */
data class FileSysNode(
    val name: String,
    val size: Long? = null,            // file size; folders can be null
    val lastModified: Long? = null,    // epoch millis；It is null when it cannot be obtained
    val nodeType: NodeType,            // Used for sorting logic
    val folderType: FolderType = FolderType.FOLDER, // Used for UI icon display
    val description: String = "",      // For instructional instructions
    val dataSource: DataSource = DataSource.MOCK,
    val children: List<FileSysNode> = emptyList(),
    val path: String? = null
) {
    val isFolder: Boolean get() = nodeType == NodeType.FOLDER
    val isFile: Boolean get() = nodeType == NodeType.FILE
    val childCount: Int get() = children.size
}


// Define a stable structure for Category definitions
data class CategoryDef(
    val name: String, val icon: ImageVector, val type: String
)

data class FileCategory(
    val name: String,
    val icon: ImageVector,
    val type: String,
    val count: Int? = null,
    val totalSizeMb: Long? = null,
    val files: List<FileItem> = emptyList()
)
