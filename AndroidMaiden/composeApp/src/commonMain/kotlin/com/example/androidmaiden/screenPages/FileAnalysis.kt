package com.example.androidmaiden.screenPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.ui.icons.fileTypeIcon
import com.example.androidmaiden.views.fileSys.*
import org.jetbrains.compose.ui.tooling.preview.Preview

class FileAnalysis {
}
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

@Preview
@Composable
fun FileAnalysisScreen() {
    var viewMode by remember { mutableStateOf(ViewMode.LIST) }

    // 模拟文件结构
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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("文件分析", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(8.dp))
        Text(
            "当前展示数据类型：教学模拟用（Android 文件系统结构）",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(Modifier.height(16.dp))

        // 视图切换按钮
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ViewMode.entries.forEach { mode ->
                FilterChip(
                    selected = viewMode == mode,
                    onClick = { viewMode = mode },
                    label = { Text(mode.name) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 根据视图模式渲染
        when (viewMode) {
            ViewMode.LIST -> FileListView(androidFileSystemMock)
            ViewMode.GRID -> FileGridView(androidFileSystemMock)
            ViewMode.TREE -> FileTreeView(androidFileSystemMock)
        }
    }
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
                if (node.isDirectory && node.description.isNotBlank()) {
                    Text(
                        node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
