package com.example.androidmaiden.views.panel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.ui.icons.ToolbarIcon
import com.example.androidmaiden.ui.icons.toolbarIcon
import com.example.androidmaiden.views.fileSys.*
import org.jetbrains.compose.ui.tooling.preview.Preview

//class FileManager {
//}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FileAnalysisToolbarPreview() {
    Surface {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FileAnalysisToolbar(
                viewMode = ViewMode.LIST,
                onViewModeChange = {},
                isAndroid = true,
                sortMode = SortMode.NAME,
                onSortModeChange = {},
                sortOrder = SortOrder.ASC,
                onSortOrderChange = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FAToolBarAndroidPreview() {
    Surface {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FAToolBarAndroid(
                viewMode = ViewMode.LIST,
                onViewModeChange = {},
                sortMode = SortMode.NAME,
                onSortModeChange = {},
                sortOrder = SortOrder.ASC,
                onSortOrderChange = {},
                useMock = true,
                onUseMockChange = {},
            )
        }
    }
}

@Composable
fun FileAnalysisToolbar(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    isAndroid: Boolean,
    useMock: Boolean = true,
    onUseMockChange: (Boolean) -> Unit = {},
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
            sortOrder = sortOrder,
            onSortOrderChange = onSortOrderChange
        )
    } else {
        // ✅ 通用实现：上下两行
        FAToolBarGeneral(viewMode, onViewModeChange, sortMode, onSortModeChange)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAToolBarAndroid(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,                  // NEW
    onSortOrderChange: (SortOrder) -> Unit,// NEW
    useMock: Boolean,
    onUseMockChange: (Boolean) -> Unit
) {
    // These actions are placed directly in the RowScope of the parent TopAppBar
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
        tooltip = { PlainTooltip { Text(stringResource(id = "toggle_simulation_real_data")) } },
        state = rememberTooltipState()
    ) {
        Switch(checked = !useMock, onCheckedChange = { onUseMockChange(!useMock) })
    }
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
                PlainTooltip { Text(stringResource(id = "toggle_view_mode")) }
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
                    contentDescription = stringResource(id = "toggle_view_to", viewMode.name),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    } else {
        ViewMode.entries.forEach { mode ->
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text(stringResource(id = "switch_to", mode.name)) } }, state = tooltipState
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
        tooltip = { PlainTooltip { Text(stringResource(id = "select_sort_method")) } },
        state = tooltipState
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(toolbarIcon(ToolbarIcon.SORT)!!, contentDescription = stringResource(id = "sort"))
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
            text = { Text(if (sortOrder == SortOrder.ASC) stringResource(id = "ascending") else stringResource(id = "descending")) },
            onClick = {
                val next = if (sortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC
                sortOrderChange(next)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = "folders_first")) },
            onClick = {
                // 可选：将 foldersFirst 也纳入 UI（如放在 ViewModel 中）
            }
        )
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
            Text(stringResource(id = "sort_by"))
            SortMode.entries.forEach { mode ->
                FilterChip(
                    selected = sortMode == mode,
                    onClick = { onSortModeChange(mode) },
                    label = { Text(mode.label) })
            }
        }
    }
}
