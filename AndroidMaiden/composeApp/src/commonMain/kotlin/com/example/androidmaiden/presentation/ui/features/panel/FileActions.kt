package com.example.androidmaiden.presentation.ui.features.panel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.util.*
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode

/**
 * Composable providing file-related actions like changing view mode and sorting.
 */
@Composable
fun FileActions(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    onSortModeChange: (SortMode) -> Unit,
    sortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    isAndroid: Boolean
) {
    ViewChange(
        onViewModeChange = onViewModeChange,
        viewMode = viewMode,
        isAndroid = isAndroid
    )
    SortBy(
        sortModeChange = onSortModeChange,
        sortOrder = sortOrder,
        sortOrderChange = onSortOrderChange
    )
}


/**
 * Composable for toggling between different view modes (List, Grid, Tree).
 */
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
                        ViewMode.LIST -> Icons.AutoMirrored.Filled.List
                        ViewMode.GRID -> Icons.Default.GridView
                        ViewMode.TREE -> Icons.Default.AccountTree
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
                            ViewMode.LIST -> Icons.AutoMirrored.Filled.List
                            ViewMode.GRID -> Icons.Default.GridView
                            ViewMode.TREE -> Icons.Default.AccountTree
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

/**
 * Composable for selecting the sorting method and order.
 */
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
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = stringResource(id = "sort"))
        }
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        SortMode.entries.forEach { mode ->
            DropdownMenuItem(
                text = { Text(mode.label) },
                onClick = {
                    sortModeChange(mode)
                    expanded = false
                }
            )
        }
        HorizontalDivider()
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
                // TODO: Optional: include foldersFirst in UI
            }
        )
    }
}
