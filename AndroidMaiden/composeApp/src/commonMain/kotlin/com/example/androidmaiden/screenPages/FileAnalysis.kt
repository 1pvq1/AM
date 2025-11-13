package com.example.androidmaiden.screenPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.mods.RequestStoragePermission
import com.example.androidmaiden.mods.listFiles
import com.example.androidmaiden.ui.icons.folderTypeIcon
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.views.others.simFileNode
import com.example.androidmaiden.views.panel.*
import kotlinx.datetime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

//class FileAnalysis {}


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
    Surface(
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            folderTypeIcon(node.folderType)?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = node.name,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(Modifier.padding(8.dp)) {
                Text(node.name, style = MaterialTheme.typography.bodyLarge)

                if (node.isFolder) {
                    // 文件夹说明
                    if (node.description.isNotBlank()) {
                        Text(
                            node.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    // ✅ 文件夹内容统计
                    val folderCount = node.children.count { it.isFolder }
                    val fileCount = node.children.count { !it.isFolder }
                    Text(
                        "$folderCount 个文件夹 · $fileCount 个文件",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    val timeText = node.lastModified?.let { formatDateTime(it) } ?: "未知时间"

                    Text(
//                        "$sizeText · $timeText",
                        " $timeText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    // 文件信息：大小 + 修改时间
//                    val sizeText = node.size?.let { formatFileSize(it) } ?: "未知大小"
                    val timeText = node.lastModified?.let { formatDateTime(it) } ?: "未知时间"

                    Text(
//                        "$sizeText · $timeText",
                        " $timeText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

// 文件大小格式化
//fun formatFileSize(size: Long): String {
//    if (size <= 0) return "0 B"
//    val units = arrayOf("B", "KB", "MB", "GB", "TB")
//    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
//    val value = size / 1024.0.pow(digitGroups.toDouble())
//    return "${"%.1f".format(value)} ${units[digitGroups]}"
//}

// 时间戳格式化
@OptIn(ExperimentalTime::class)
fun formatDateTime(epochMillis: Long?): String {
    if (epochMillis == null) return "未知时间"
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.year}-${local.monthNumber.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')} " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}