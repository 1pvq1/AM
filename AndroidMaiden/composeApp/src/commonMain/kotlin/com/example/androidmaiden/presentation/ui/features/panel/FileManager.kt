package com.example.androidmaiden.presentation.ui.features.panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.HiddenFilterMode
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.core.util.deprecated.SortMode
import com.example.androidmaiden.core.util.deprecated.SortOrder
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the file analysis toolbar in an Android context.
 */
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
                onSortOrderChange = {},
                hiddenFilterMode = HiddenFilterMode.SHOW_ALL,
                onHiddenFilterModeChange = {}
            )
        }
    }
}

/**
 * Preview specifically for the Android implementation of the toolbar.
 */
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
                hiddenFilterMode = HiddenFilterMode.SHOW_ALL,
                onHiddenFilterModeChange = {}
            )
        }
    }
}

/**
 * A toolbar for the file analysis screen that provides view mode, sorting, and hidden file filtering options.
 */
@Composable
fun FileAnalysisToolbar(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    isAndroid: Boolean,
    useMock: Boolean = true,
    onUseMockChange: (Boolean) -> Unit = {},
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    hiddenFilterMode: HiddenFilterMode = HiddenFilterMode.SHOW_ALL,
    onHiddenFilterModeChange: (HiddenFilterMode) -> Unit = {}
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
            onSortOrderChange = onSortOrderChange,
            hiddenFilterMode = hiddenFilterMode,
            onHiddenFilterModeChange = onHiddenFilterModeChange
        )
    } else {
        FAToolBarGeneral(viewMode, onViewModeChange, sortMode, onSortModeChange)
    }
}

/**
 * Android-specific implementation of the file analysis toolbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAToolBarAndroid(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    useMock: Boolean,
    onUseMockChange: (Boolean) -> Unit,
    hiddenFilterMode: HiddenFilterMode,
    onHiddenFilterModeChange: (HiddenFilterMode) -> Unit
) {
    var filterMenuExpanded by remember { mutableStateOf(false) }

    FileActions(
        viewMode = viewMode,
        onViewModeChange = onViewModeChange,
        onSortModeChange = onSortModeChange,
        sortOrder = sortOrder,
        onSortOrderChange = onSortOrderChange,
        isAndroid = true
    )

    // Hidden File Filter Menu Button
    Box {
        IconButton(onClick = { filterMenuExpanded = true }) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Hidden Data Filter",
                tint = if (hiddenFilterMode != HiddenFilterMode.SHOW_ALL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        DropdownMenu(
            expanded = filterMenuExpanded,
            onDismissRequest = { filterMenuExpanded = false }
        ) {
            HiddenFilterMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode.label) },
                    onClick = {
                        onHiddenFilterModeChange(mode)
                        filterMenuExpanded = false
                    },
                    trailingIcon = {
                        if (hiddenFilterMode == mode) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                )
            }
        }
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(stringResource(id = "toggle_simulation_real_data")) } },
        state = rememberTooltipState()
    ) {
        Switch(checked = !useMock, onCheckedChange = { onUseMockChange(!useMock) })
    }
}

/**
 * General implementation of the file analysis toolbar for other platforms.
 */
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

/**
 * Preview for the general implementation of the toolbar.
 */
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
