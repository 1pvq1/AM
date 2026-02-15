package com.example.androidmaiden.views.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.utils.SortMode
import com.example.androidmaiden.utils.SortOrder
import com.example.androidmaiden.views.fileSys.ViewMode
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    FileActions(
        viewMode = viewMode,
        onViewModeChange = onViewModeChange,
        onSortModeChange = onSortModeChange,
        sortOrder = sortOrder,
        onSortOrderChange = onSortOrderChange,
        isAndroid = true
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

@Preview
@Composable
private fun FAToolBarGeneralPreview() {
    FAToolBarGeneral(
        viewMode = ViewMode.LIST,
        onViewModeChange = {},
        sortMode = SortMode.NAME,
        onSortModeChange = {}
    )
}
