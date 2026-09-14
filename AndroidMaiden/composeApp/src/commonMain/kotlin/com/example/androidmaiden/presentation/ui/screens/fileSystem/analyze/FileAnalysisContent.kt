package com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.viewmodel.FolderAnalysisStats
import com.example.androidmaiden.presentation.ui.screens.pages.BasePage
import com.example.androidmaiden.presentation.ui.features.fileSys.*
import com.example.androidmaiden.presentation.ui.features.panel.*
import com.example.androidmaiden.core.util.deprecated.SortMode
import com.example.androidmaiden.core.util.deprecated.SortOrder
import com.example.androidmaiden.core.util.deprecated.sortedChildren
import com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze.components.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.platform.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

/**
 * Stateless content for the File System Analysis feature.
 *
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class, ExperimentalFoundationApi::class)
@Composable
fun FileAnalysisContent(
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
    // --- Interaction State ---
    var selectedNode by remember { mutableStateOf<FileSysNode?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showStatsPopUp by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BasePage(
        title = stringResource(id = "file_analysis"),
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = onBack,
        scrollBehavior = scrollBehavior,
        actions = {
            // Stats Trigger
            IconButton(onClick = { showStatsPopUp = true }) {
                Icon(Icons.Default.BarChart, contentDescription = "Folder Stats")
            }
            // Sorting & View Mode Toolbar
            FileAnalysisToolbar(
                viewMode = viewMode,
                onViewModeChange = onViewModeChange,
                useMock = useMock,
                onUseMockChange = { onToggleSource() },
                isAndroid = true,
                sortMode = sortMode,
                onSortModeChange = onSortModeChange,
                sortOrder = sortOrder,
                onSortOrderChange = onSortOrderChange
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            // Navigation Breadcrumbs
            PathBreadcrumbs(
                pathStack = pathStack,
                onIndexClick = onNavigateToStackIndex,
                onRootClick = { onNavigateToStackIndex(-1) }
            )

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            // Main Content Area
            Box(modifier = Modifier.weight(1f)) {
                when {
                    isLoading -> {
                        FileAnalysisSkeleton()
                    }

                    loadError != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = loadError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    root != null -> {
                        val sortedRoot = root.copy(
                            children = root.sortedChildren(mode = sortMode, order = sortOrder)
                        )

                        // Interaction handlers
                        val onNodeClick: (FileSysNode) -> Unit = { node ->
                            selectedNode = node
                        }
                        
                        val onNodeDoubleClick: (FileSysNode) -> Unit = { node ->
                            if (node.isFolder) {
                                onNavigateTo(node)
                            } else {
                                selectedNode = node
                            }
                        }

                        // Visualization Content
                        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                            when (viewMode) {
                                ViewMode.LIST -> FileListView(sortedRoot, onNodeClick, onNodeDoubleClick)
                                ViewMode.GRID -> FileGridView(sortedRoot, onNodeClick, onNodeDoubleClick)
                                ViewMode.TREE -> FileTreeView(sortedRoot, onNodeClick, onNodeDoubleClick)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Overlays, Sheets & Dialogs ---

    // 1. Statistics Visualization
    if (showStatsPopUp) {
        StatsPopUp(stats = stats, onDismiss = { showStatsPopUp = false })
    }

    // 2. Node Info & Action Sheet
    selectedNode?.let { node ->
        ModalBottomSheet(onDismissRequest = { selectedNode = null }) {
            FileActionSheetContent(
                node = node,
                onEnterClick = { 
                    if (node.isFolder) onNavigateTo(node)
                    selectedNode = null
                },
                onRenameClick = { showRenameDialog = true },
                onDeleteClick = { showDeleteDialog = true }
            )
        }
    }

    // 3. Delete Confirmation Dialog
    if (showDeleteDialog && selectedNode != null) {
        DeleteConfirmDialog(
            node = selectedNode!!,
            onConfirm = {
                onDeleteNode(selectedNode!!)
                showDeleteDialog = false
                selectedNode = null
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    // 4. Rename Action Dialog
    if (showRenameDialog && selectedNode != null) {
        RenameDialog(
            node = selectedNode!!,
            onConfirm = { newName ->
                onRenameNode(selectedNode!!, newName)
                showRenameDialog = false
                selectedNode = null
            },
            onDismiss = { showRenameDialog = false }
        )
    }
}

/**
 * Light theme preview for the file analysis content.
 */
@ExperimentalTime
@Preview
@Composable
fun FileAnalysisContentLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileAnalysisContent(
                root = FileSysNode(name = "Root", nodeType = NodeType.FOLDER),
                stats = FolderAnalysisStats(),
                isLoading = false,
                loadError = null,
                pathStack = listOf("Storage", "Documents"),
                useMock = true,
                viewMode = ViewMode.LIST,
                sortMode = SortMode.NAME,
                sortOrder = SortOrder.ASC,
                onBack = {},
                onNavigateTo = {},
                onToggleSource = {},
                onViewModeChange = {},
                onSortModeChange = {},
                onSortOrderChange = {},
                onNavigateToStackIndex = {},
                onDeleteNode = {},
                onRenameNode = { _, _ -> }
            )
        }
    }
}
