package com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.core.util.deprecated.SortMode
import com.example.androidmaiden.core.util.deprecated.SortOrder
import com.example.androidmaiden.presentation.viewmodel.FolderAnalysisStats
import org.jetbrains.compose.ui.tooling.preview.Preview
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
 * @param hiddenFilterMode System hidden files filter mode.
 * @param onBack Callback for navigating back.
 * @param onNavigateTo Callback to navigate to a specific node.
 * @param onToggleSource Callback to switch between mock and real data.
 * @param onViewModeChange Callback when the view mode is changed.
 * @param onSortModeChange Callback when the sort mode is changed.
 * @param onSortOrderChange Callback when the sort order is changed.
 * @param onHiddenFilterModeChange Callback when the hidden files filter mode is changed.
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
    hiddenFilterMode: HiddenFilterMode = HiddenFilterMode.SHOW_ALL,
    onBack: () -> Unit,
    onNavigateTo: (FileSysNode) -> Unit,
    onToggleSource: () -> Unit,
    onViewModeChange: (ViewMode) -> Unit,
    onSortModeChange: (SortMode) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onHiddenFilterModeChange: (HiddenFilterMode) -> Unit = {},
    onNavigateToStackIndex: (Int) -> Unit,
    onDeleteNode: (FileSysNode) -> Unit,
    onRenameNode: (FileSysNode, String) -> Unit
) {
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
        hiddenFilterMode = hiddenFilterMode,
        onBack = onBack,
        onNavigateTo = onNavigateTo,
        onToggleSource = onToggleSource,
        onViewModeChange = onViewModeChange,
        onSortModeChange = onSortModeChange,
        onSortOrderChange = onSortOrderChange,
        onHiddenFilterModeChange = onHiddenFilterModeChange,
        onNavigateToStackIndex = onNavigateToStackIndex,
        onDeleteNode = onDeleteNode,
        onRenameNode = onRenameNode
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

private val sampleAnalysisRoot = FileSysNode(
    name = "Documents",
    nodeType = NodeType.FOLDER,
    folderType = FolderType.FOLDER,
    path = "/storage/emulated/0/Documents",
    children = listOf(
        FileSysNode(
            name = "ProjectSpec.pdf",
            size = 2_450_000L,
            nodeType = NodeType.FILE,
            folderType = FolderType.DOCUMENT,
            path = "/storage/emulated/0/Documents/ProjectSpec.pdf"
        ),
        FileSysNode(
            name = "SourceCode.zip",
            size = 18_200_000L,
            nodeType = NodeType.FILE,
            folderType = FolderType.OTHER,
            path = "/storage/emulated/0/Documents/SourceCode.zip"
        ),
        FileSysNode(
            name = "Presentation.pptx",
            size = 12_800_000L,
            nodeType = NodeType.FILE,
            folderType = FolderType.DOCUMENT,
            path = "/storage/emulated/0/Documents/Presentation.pptx"
        )
    )
)

private val sampleAnalysisStats = FolderAnalysisStats(
    typeDistribution = mapOf("pdf" to 2_450_000L, "zip" to 18_200_000L, "pptx" to 12_800_000L),
    totalSize = 33_450_000L,
    fileCount = 3,
    folderCount = 0
)

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun FileAnalysisCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileAnalysisCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Compact,
                    heightCategory = WindowSizeCategory.Medium
                ),
                root = sampleAnalysisRoot,
                stats = sampleAnalysisStats,
                isLoading = false,
                loadError = null,
                pathStack = listOf("Storage", "Documents"),
                useMock = true,
                viewMode = ViewMode.LIST,
                sortMode = SortMode.NAME,
                sortOrder = SortOrder.ASC,
                hiddenFilterMode = HiddenFilterMode.SHOW_ALL,
                onBack = {},
                onNavigateTo = {},
                onToggleSource = {},
                onViewModeChange = {},
                onSortModeChange = {},
                onSortOrderChange = {},
                onHiddenFilterModeChange = {},
                onNavigateToStackIndex = {},
                onDeleteNode = {},
                onRenameNode = { _, _ -> }
            )
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun FileAnalysisCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileAnalysisCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Medium,
                    heightCategory = WindowSizeCategory.Medium
                ),
                root = sampleAnalysisRoot,
                stats = sampleAnalysisStats,
                isLoading = false,
                loadError = null,
                pathStack = listOf("Storage", "Documents"),
                useMock = true,
                viewMode = ViewMode.GRID,
                sortMode = SortMode.SIZE,
                sortOrder = SortOrder.DESC,
                hiddenFilterMode = HiddenFilterMode.SHOW_ALL,
                onBack = {},
                onNavigateTo = {},
                onToggleSource = {},
                onViewModeChange = {},
                onSortModeChange = {},
                onSortOrderChange = {},
                onHiddenFilterModeChange = {},
                onNavigateToStackIndex = {},
                onDeleteNode = {},
                onRenameNode = { _, _ -> }
            )
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun FileAnalysisCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileAnalysisCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Expanded,
                    heightCategory = WindowSizeCategory.Expanded
                ),
                root = sampleAnalysisRoot,
                stats = sampleAnalysisStats,
                isLoading = false,
                loadError = null,
                pathStack = listOf("Storage", "Documents"),
                useMock = true,
                viewMode = ViewMode.TREE,
                sortMode = SortMode.NAME,
                sortOrder = SortOrder.ASC,
                hiddenFilterMode = HiddenFilterMode.SHOW_ALL,
                onBack = {},
                onNavigateTo = {},
                onToggleSource = {},
                onViewModeChange = {},
                onSortModeChange = {},
                onSortOrderChange = {},
                onHiddenFilterModeChange = {},
                onNavigateToStackIndex = {},
                onDeleteNode = {},
                onRenameNode = { _, _ -> }
            )
        }
    }
}

