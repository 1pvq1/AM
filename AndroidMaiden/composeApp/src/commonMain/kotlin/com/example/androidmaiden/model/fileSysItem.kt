package com.example.androidmaiden.model

// Todo： future
//enum class FileType {
//    FOLDER, DOCUMENT, IMAGE, MUSIC, VIDEO, OTHER
//}

//oldFileNode{
//    val type: NodeType,
//    val isMock: Boolean = true,
//    val isDirectory: Boolean,
//    val type: FileType = if (isDirectory) FileType.FOLDER else FileType.OTHER,
//}

// 文件夹类别，用于显示特定文件夹图标以区分不同于默认文件夹图标
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
    val size: Long? = null,            // 文件字节大小；文件夹可为 null
    val lastModified: Long? = null,    // epoch millis；不可得时为 null
    val nodeType: NodeType,            // ✅ 用于排序逻辑
    val folderType: FolderType = FolderType.FOLDER, // ✅ 用于 UI 图标显示
    val description: String = "",      // 用于教学说明
    val dataSource: DataSource = DataSource.MOCK,
    val children: List<FileNode> = emptyList(),
    val path: String? = null
) {
    val isFolder: Boolean get() = nodeType == NodeType.FOLDER
    val isFile: Boolean get() = nodeType == NodeType.FILE
    val childCount: Int get() = children.size
}
