package com.example.androidmaiden.model

// Folder categories that display specific folder icons to distinguish them from the default folder icons
enum class FolderType {
    FOLDER, DOCUMENT, IMAGE, MUSIC, VIDEO, OTHER
}

enum class NodeType { FILE, FOLDER }
enum class DataSource { MOCK, REAL }

/**
 * Core data structure for representing a file or folder.
 */
data class FileNode(
    val name: String,
    val size: Long? = null,            // file size; folders can be null
    val lastModified: Long? = null,    // epoch millis；It is null when it cannot be obtained
    val nodeType: NodeType,            // Used for sorting logic
    val folderType: FolderType = FolderType.FOLDER, // Used for UI icon display
    val description: String = "",      // For instructional instructions
    val dataSource: DataSource = DataSource.MOCK,
    val children: List<FileNode> = emptyList(),
    val path: String? = null
) {
    val isFolder: Boolean get() = nodeType == NodeType.FOLDER
    val isFile: Boolean get() = nodeType == NodeType.FILE
    val childCount: Int get() = children.size
}
