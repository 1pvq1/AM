package com.example.androidmaiden.screens.fileSystem.analyze

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
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.model.*
import com.example.androidmaiden.screens.pages.BasePage
import com.example.androidmaiden.utils.*
import com.example.androidmaiden.viewModels.FileScannerViewModel
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.views.panel.FileAnalysisToolbar
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

/**
 * Main screen for the File System Analysis feature.
 * This screen provides a hierarchical view of the file system (Mock or Real)
 * and allows users to perform basic file operations and view storage statistics.
 *
 * The screen is composed of:
 * - A top bar with navigation and sorting/view mode controls.
 * - Breadcrumbs for quick navigation through the directory hierarchy.
 * - A dynamic content area supporting List, Grid, and Tree views.
 * - Overlays for statistics, file actions, and confirmation dialogs.
 *
 * @param onNavigateUp Callback to navigate back to the previous major screen/page.
 * @param vm The ViewModel for this screen, provided by Koin by default.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class, ExperimentalFoundationApi::class)
@Composable
fun FileAnalysisScreen(
    onNavigateUp: () -> Unit = {},
    vm: FileScannerViewModel = koinViewModel()
) {

    // --- UI State ---
    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASC) }

    // --- ViewModel & Data ---
    // vm is now hoisted to the function parameters to allow easier testing and previews.
    val useMock = vm.useMock
    
    val root = vm.currentDirectory
    val stats = vm.folderStats
    val isLoading by vm.isLoading.collectAsState()
    val loadError by vm.error.collectAsState()
    val pathStack = vm.pathStack

    // --- Interaction State ---
    var selectedNode by remember { mutableStateOf<FileSysNode?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showStatsPopUp by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BasePage(
        title = stringResource(id = "file_analysis"),
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = {
            if (!vm.navigateBack()) {
                onNavigateUp()
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            // Stats Trigger
            IconButton(onClick = { showStatsPopUp = true }) {
                Icon(Icons.Default.BarChart, contentDescription = "Folder Stats")
            }
            // Sorting & View Mode Toolbar
            FileAnalysisToolbar(
                viewMode = viewMode,
                onViewModeChange = { viewMode = it },
                useMock = useMock,
                onUseMockChange = { vm.toggleSource() },
                isAndroid = true, 
                sortMode = sortMode,
                onSortModeChange = { sortMode = it },
                sortOrder = sortOrder,
                onSortOrderChange = { sortOrder = it }
            )
        }
    ) { innerPadding ->
        // Start incremental sync when entering the screen to ensure real data is available
        LaunchedEffect(useMock) {
            if (!useMock) {
                vm.startSync()
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            // Navigation Breadcrumbs
            PathBreadcrumbs(
                pathStack = pathStack,
                onIndexClick = { vm.navigateToStackIndex(it) },
                onRootClick = { vm.navigateToStackIndex(-1) }
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
                                text = loadError!!,
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
                                vm.navigateTo(node)
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
                    if (node.isFolder) vm.navigateTo(node)
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
                selectedNode?.let { vm.deleteNode(it) }
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
                selectedNode?.let { vm.renameNode(it, newName) }
                showRenameDialog = false
                selectedNode = null
            },
            onDismiss = { showRenameDialog = false }
        )
    }
}

