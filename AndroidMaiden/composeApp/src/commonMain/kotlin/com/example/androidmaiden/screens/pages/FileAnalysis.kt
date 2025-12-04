package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.mods.RequestStoragePermission
import com.example.androidmaiden.mods.listFiles
import com.example.androidmaiden.model.*
import com.example.androidmaiden.ui.icons.*
import com.example.androidmaiden.utils.formatDateTime
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.views.eg.simFileNode
import com.example.androidmaiden.views.panel.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun FileAnalysisScreen() {

    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASC) }
    var useMock by remember { mutableStateOf(true) }

    val root = if (useMock) simFileNode()
    else FileNode(
        name = "/",
        nodeType = NodeType.FOLDER,
        folderType = FolderType.FOLDER,
        dataSource = DataSource.REAL,
        children = listFiles("/storage/emulated/0"), // 公共存储根目录
        lastModified = Clock.System.now().toEpochMilliseconds(),
        description = "真实设备根目录",
        path = "/storage/emulated/0"
    )

    // ✅ 进入页面时触发权限请求
    RequestStoragePermission()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(8.dp),
        topBar = {
            FileAnalysisToolbar(
                viewMode = viewMode,
                onViewModeChange = { viewMode = it },
                useMock = useMock,
                onUseMockChange = { useMock = it },
                scrollBehavior = scrollBehavior,
                isAndroid = true, // ✅ 这里可以用 expect/actual 或 Build check
                sortMode = sortMode,
                onSortModeChange = { sortMode = it },
                sortOrder = sortOrder,
                onSortOrderChange = { sortOrder = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                if (useMock) "当前展示数据类型：教学模拟用（Android 文件系统结构）"
                else "当前展示数据类型：真实文件系统（Android）",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // 根据 sortMode 对文件排序
            val sortedRoot = root.copy(
                children = root.sortedChildren(mode = sortMode, order = sortOrder)
            )

            // 根据视图模式渲染
            when (viewMode) {
                ViewMode.LIST -> FileListView(sortedRoot)
                ViewMode.GRID -> FileGridView(sortedRoot)
                ViewMode.TREE -> FileTreeView(sortedRoot)
            }
        }
    }
}


@Composable
fun FileItem(node: FileNode, modifier: Modifier = Modifier) {
    val icon = if (node.isFolder) {
        folderTypeIcon(node.folderType)
    } else {
        fileTypeIcon(node.name)
    }

    ListItem(
        headlineContent = { Text(node.name, style = MaterialTheme.typography.bodyLarge) },
        supportingContent = {
            Column {
                if (node.description.isNotBlank()) {
                    Text(
                        node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                val timeText = node.lastModified?.let { formatDateTime(it) } ?: "未知时间"
                if (node.isFolder) {
                    val folderCount = node.children.count { it.isFolder }
                    val fileCount = node.children.count { !it.isFolder }
                    Text(
                        "$folderCount 文件夹 · $fileCount 文件 · $timeText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Text(
                        timeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = node.name,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}
