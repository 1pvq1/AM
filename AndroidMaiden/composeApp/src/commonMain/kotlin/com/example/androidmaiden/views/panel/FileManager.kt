package com.example.androidmaiden.views.panel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.ui.icons.ToolbarIcon
import com.example.androidmaiden.ui.icons.toolbarIcon
import com.example.androidmaiden.views.fileSys.*

//class FileManager {
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileAnalysisToolbar(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    isAndroid: Boolean,
    useMock: Boolean = true,
    onUseMockChange: (Boolean) -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,                  // NEW
    onSortOrderChange: (SortOrder) -> Unit,// NEW

) {
    if (isAndroid) {
        FAToolBarAndroid(
            viewMode = viewMode,
            onViewModeChange = onViewModeChange,
            sortMode = sortMode,
            onSortModeChange = onSortModeChange,
            useMock = useMock,
            onUseMockChange = onUseMockChange,
            scrollBehavior = scrollBehavior,
            sortOrder = sortOrder,
            onSortOrderChange = onSortOrderChange
        )
    } else {
        // ✅ 通用实现：上下两行
        FAToolBarGeneral(viewMode, onViewModeChange, sortMode, onSortModeChange)
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FAToolBarAndroid(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,                  // NEW
    onSortOrderChange: (SortOrder) -> Unit,// NEW
    useMock: Boolean,
    onUseMockChange: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    TopAppBar(
        title = { Text("文件分析") }, actions = {
            // 视图切换图标
            ViewChange(
                onViewModeChange = onViewModeChange, viewMode = viewMode, isAndroid = true
            )

            // 排序图标
            SortBy(
                sortModeChange = onSortModeChange,
                sortOrder = sortOrder,
                sortOrderChange = onSortOrderChange
            )

            // 模拟/真实数据切换
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text("切换模拟/真实数据") } },
                state = rememberTooltipState()
            ) {
                Switch(checked = !useMock, onCheckedChange = { onUseMockChange(!useMock) })
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewChange(
    onViewModeChange: (ViewMode) -> Unit,
    viewMode: ViewMode,
    isAndroid: Boolean
) {
    val tooltipState = rememberTooltipState()

    if (isAndroid) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip { Text("切换视图模式") }
            },
            state = tooltipState
        ) {
            IconButton(onClick = {
                val next = when (viewMode) {
                    ViewMode.LIST -> ViewMode.GRID
                    ViewMode.GRID -> ViewMode.TREE
                    ViewMode.TREE -> ViewMode.LIST
                }
                onViewModeChange(next)
            }) {
                Icon(
                    imageVector = when (viewMode) {
                        ViewMode.LIST -> toolbarIcon(ToolbarIcon.LIST)!!
                        ViewMode.GRID -> toolbarIcon(ToolbarIcon.GRID)!!
                        ViewMode.TREE -> toolbarIcon(ToolbarIcon.TREE)!!
                    },
                    contentDescription = "切换视图为 ${viewMode.name}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    } else {
        ViewMode.entries.forEach { mode ->
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text("切换到 ${mode.name}") } }, state = tooltipState
            ) {
                IconButton(onClick = { onViewModeChange(mode) }) {
                    Icon(
                        imageVector = when (mode) {
                            ViewMode.LIST -> toolbarIcon(ToolbarIcon.LIST)!!
                            ViewMode.GRID -> toolbarIcon(ToolbarIcon.GRID)!!
                            ViewMode.TREE -> toolbarIcon(ToolbarIcon.TREE)!!
                        },
                        contentDescription = mode.name,
                        tint = if (viewMode == mode)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBy(
    sortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,
    sortOrderChange: (SortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val tooltipState = rememberTooltipState()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text("选择排序方式") } },
        state = tooltipState
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(toolbarIcon(ToolbarIcon.SORT)!!, contentDescription = "排序")
        }
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        SortMode.values().forEach { mode ->
            DropdownMenuItem(
                text = { Text(mode.label) },
                onClick = {
                    sortModeChange(mode)
                    expanded = false
                }
            )
        }
        Divider()
        DropdownMenuItem(
            text = { Text(if (sortOrder == SortOrder.ASC) "升序" else "降序") },
            onClick = {
                val next = if (sortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC
                sortOrderChange(next)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("文件夹优先") },
            onClick = {
                // 可选：将 foldersFirst 也纳入 UI（如放在 ViewModel 中）
            }
        )
    }
}

//@Composable
//fun SortBy(onSortModeChange: (SortMode) -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//    IconButton(onClick = { expanded = true }) {
//        Icon(toolbarIcon(ToolbarIcon.SORT)!!, contentDescription = "排序")
//    }
//    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//        SortMode.entries.forEach { mode ->
//            DropdownMenuItem(text = { Text(mode.label) }, onClick = {
//                onSortModeChange(mode)
//                expanded = false
//            })
//        }
//    }
//}


//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//private fun FAToolBarAndroid(
//    onViewModeChange: (ViewMode) -> Unit,
//    viewMode: ViewMode,
//    onSortModeChange: (SortMode) -> Unit
//) {
//    Column {
//        // 1. ✅ 安卓定制：TopAppBar
//        TopAppBar(title = { Text("文件分析") })
//
//        // 2. 功能区
//        Column(
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // 视图切换按钮行
//            ViewChange()
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("排序：", style = MaterialTheme.typography.labelLarge)
//                // 排序菜单
//                SortBy(
//                    onSortModeChange = onSortModeChange
//                )
//            }
//        }
//    }
//}


@Composable
private fun FAToolBarGeneral(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ViewMode.entries.forEach { mode ->
                FilterChip(
                    selected = viewMode == mode,
                    onClick = { onViewModeChange(mode) },
                    label = { Text(mode.name) })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("排序：")
            SortMode.entries.forEach { mode ->
                FilterChip(
                    selected = sortMode == mode,
                    onClick = { onSortModeChange(mode) },
                    label = { Text(mode.label) })
            }
        }
    }
}
