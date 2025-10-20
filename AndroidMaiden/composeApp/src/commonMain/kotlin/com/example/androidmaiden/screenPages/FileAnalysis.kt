package com.example.androidmaiden.screenPages

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.mods.RequestStoragePermission
import com.example.androidmaiden.mods.listFiles
import com.example.androidmaiden.ui.icons.fileTypeIcon
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.views.panel.FileAnalysisToolbar
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.collections.sortedWith

//class FileAnalysis {}

enum class FileType {
    FOLDER, DOCUMENT, IMAGE, MUSIC, VIDEO, OTHER
}

data class FileNode(
    val name: String,
    val isDirectory: Boolean,
    val children: List<FileNode> = emptyList(),
    val description: String = "", // 用于教学说明
    val isMock: Boolean = true,
    val type: FileType = if (isDirectory) FileType.FOLDER else FileType.OTHER
)


enum class ViewMode { LIST, GRID, TREE }

@Preview(showBackground = true)
@Composable
fun FileAnalysisScreen() {
    RequestStoragePermission()

    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var useMock by remember { mutableStateOf(true) }
    val root = if (useMock) simFileNode()
    else FileNode(
        "/", true,
//        children = listFiles("/"),
        children = listFiles("/storage/emulated/0"), // 公共存储根目录
        isMock = false
    )

    // ✅ 进入页面时触发权限请求
    RequestStoragePermission()

    Column(
        Modifier.fillMaxSize().padding(16.dp).scrollable(
            state = rememberScrollableState { it },
            orientation = Orientation.Vertical,
            enabled = true
        ), verticalArrangement = Arrangement.Top
    ) {
        FileAnalysisToolbar(
            viewMode = viewMode,
            onViewModeChange = { viewMode = it },
            sortMode = sortMode,
            onSortModeChange = { sortMode = it },
            isAndroid = true // ✅ 这里可以用 expect/actual 或 Build check
        )
//        Text("文件分析", style = MaterialTheme.typography.headlineSmall)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("模拟/真实 数据")
            Spacer(Modifier.width(10.dp))
            Switch(
                checked = !useMock, onCheckedChange = { useMock = !it })
        }
        Text(
            if (useMock) "当前展示数据类型：教学模拟用（Android 文件系统结构）"
            else "当前展示数据类型：真实文件系统（Android）",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(Modifier.height(16.dp))

        // 根据 sortMode 对文件排序

        val sortedChildren = root.children.sortedWith(
            when (sortMode) {
                SortMode.NAME -> compareBy { it.name.lowercase() }
                SortMode.DATE -> compareBy { /* TODO: 加入文件时间 */ it.name }
                SortMode.SIZE -> compareBy { /* TODO: 加入文件大小 */ it.name }
            }
        )
        val sortedRoot = root.copy(children = sortedChildren)

        // 根据视图模式渲染
        when (viewMode) {
            ViewMode.LIST -> FileListView(sortedRoot)
            ViewMode.GRID -> FileGridView(root)
            ViewMode.TREE -> FileTreeView(root)
        }
    }
}

// 模拟文件结构
private fun simFileNode(): FileNode {
    val androidFileSystemMock = FileNode(
        name = "/",
        isDirectory = true,
        description = "Android 文件系统根目录,这是文件系统的起点",
        children = listOf(
            FileNode(
                "storage", true, description = "存放用户可访问的存储空间", children = listOf(
                    FileNode(
                        "emulated", true, description = "模拟的内部存储", children = listOf(
                            FileNode(
                                "0", true, description = "用户主目录", children = listOf(
                                    FileNode("Download", true, description = "下载的文件"),
                                    FileNode("Documents", true, description = "文档文件"),
                                    FileNode("Pictures", true, description = "图片文件"),
                                    FileNode("Music", true, description = "音乐文件")
                                )
                            )
                        )
                    )
                )
            ),
            FileNode("system", true, description = "系统核心文件（只读，用户不可修改）"),
            FileNode("data", true, description = "应用数据目录，每个应用有独立沙盒"),
            FileNode("vendor", true, description = "硬件厂商相关文件")
        )
    )
    return androidFileSystemMock
}


@Composable
fun FileItem(node: FileNode, modifier: Modifier = Modifier) {
    Surface(
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fileTypeIcon(node.type)?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = node.name,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(Modifier.padding(8.dp)) {
                Text(node.name, style = MaterialTheme.typography.bodyLarge)

                if (node.isDirectory) {
                    // 文件夹说明
                    if (node.description.isNotBlank()) {
                        Text(
                            node.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    // ✅ 文件夹内容统计
                    val folderCount = node.children.count { it.isDirectory }
                    val fileCount = node.children.count { !it.isDirectory }
                    Text(
                        "$folderCount 个文件夹 · $fileCount 个文件",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

