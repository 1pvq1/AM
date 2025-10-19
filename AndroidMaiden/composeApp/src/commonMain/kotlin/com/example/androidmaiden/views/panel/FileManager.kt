package com.example.androidmaiden.views.panel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.screenPages.ViewMode
import com.example.androidmaiden.ui.icons.ToolbarIcon
import com.example.androidmaiden.ui.icons.toolbarIcon
import com.example.androidmaiden.views.fileSys.SortMode

//class FileManager {
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileAnalysisToolbar(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    isAndroid: Boolean
) {
    if (isAndroid) {
        Column {
            // 1. ✅ 安卓定制：TopAppBar
            TopAppBar(title = { Text("文件分析") })

            // 2. 功能区
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 视图切换按钮行
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("视图：", style = MaterialTheme.typography.labelLarge)
                    ViewMode.entries.forEach { mode ->
                        IconButton(onClick = { onViewModeChange(mode) }) {
                            Icon(
                                imageVector = when (mode) {
                                    ViewMode.LIST -> toolbarIcon(ToolbarIcon.LIST)!!
                                    ViewMode.GRID -> toolbarIcon(ToolbarIcon.GRID)!!
                                    ViewMode.TREE -> toolbarIcon(ToolbarIcon.TREE)!!
                                },
                                contentDescription = mode.name,
                                tint = if (viewMode == mode) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("排序：", style = MaterialTheme.typography.labelLarge)
                    // 排序菜单
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = toolbarIcon(ToolbarIcon.SORT)!!,
                            contentDescription = "排序"
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        SortMode.entries.forEach { mode ->
                            DropdownMenuItem(text = { Text(mode.label) }, onClick = {
                                onSortModeChange(mode)
                                expanded = false
                            })
                        }
                    }
                }
            }
        }
    } else {
        // ✅ 通用实现：上下两行
        FAToolBarGeneral(viewMode, onViewModeChange, sortMode, onSortModeChange)
    }
}

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
                    label = { Text(mode.label) }
                )
            }
        }
    }
}
