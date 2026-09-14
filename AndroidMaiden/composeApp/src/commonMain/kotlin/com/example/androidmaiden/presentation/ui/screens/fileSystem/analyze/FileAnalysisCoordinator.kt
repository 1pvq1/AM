package com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.core.util.deprecated.SortMode
import com.example.androidmaiden.core.util.deprecated.SortOrder
import com.example.androidmaiden.presentation.viewmodel.FolderAnalysisStats
import kotlin.time.ExperimentalTime

/**
 * Adaptive Coordinator for the File System Analysis feature.
 *
 * @param windowSizeClass The current window size class of the device.
 * @param root The current root directory node being analyzed.
 * @param stats Statistics for the current folder.
 * @param isLoading Whether the data is currently loading.
 * @param loadError Any error message if loading failed.
 * @param pathStack The current navigation path stack.
 * @param useMock Whether mock data is currently being used.
 * @param viewMode The current view mode (List, Grid, or Tree).
 * @param sortMode The current sort mode (Name, Size, or Date).
 * @param sortOrder The current sort order (Ascending or Descending).
 * @param onBack Callback for navigating back.
 * @param onNavigateTo Callback to navigate to a specific node.
 * @param onToggleSource Callback to switch between mock and real data.
 * @param onViewModeChange Callback when the view mode is changed.
 * @param onSortModeChange Callback when the sort mode is changed.
 * @param onSortOrderChange Callback when the sort order is changed.
 * @param onNavigateToStackIndex Callback to navigate to a specific index in the path stack.
 * @param onDeleteNode Callback to delete a file system node.
 * @param onRenameNode Callback to rename a file system node.
 */
@ExperimentalTime
@Composable
fun FileAnalysisCoordinator(
    windowSizeClass: WindowSizeClass,
    root: FileSysNode?,
    stats: FolderAnalysisStats,
    isLoading: Boolean,
    loadError: String?,
    pathStack: List<String>,
    useMock: Boolean,
    viewMode: ViewMode,
    sortMode: SortMode,
    sortOrder: SortOrder,
    onBack: () -> Unit,
    onNavigateTo: (FileSysNode) -> Unit,
    onToggleSource: () -> Unit,
    onViewModeChange: (ViewMode) -> Unit,
    onSortModeChange: (SortMode) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onNavigateToStackIndex: (Int) -> Unit,
    onDeleteNode: (FileSysNode) -> Unit,
    onRenameNode: (FileSysNode, String) -> Unit
) {
    // Analysis UI is complex and might benefit from different layouts on larger screens.
    // For now, it uses the standard responsive content.
    FileAnalysisContent(
        root = root,
        stats = stats,
        isLoading = isLoading,
        loadError = loadError,
        pathStack = pathStack,
        useMock = useMock,
        viewMode = viewMode,
        sortMode = sortMode,
        sortOrder = sortOrder,
        onBack = onBack,
        onNavigateTo = onNavigateTo,
        onToggleSource = onToggleSource,
        onViewModeChange = onViewModeChange,
        onSortModeChange = onSortModeChange,
        onSortOrderChange = onSortOrderChange,
        onNavigateToStackIndex = onNavigateToStackIndex,
        onDeleteNode = onDeleteNode,
        onRenameNode = onRenameNode
    )
}
