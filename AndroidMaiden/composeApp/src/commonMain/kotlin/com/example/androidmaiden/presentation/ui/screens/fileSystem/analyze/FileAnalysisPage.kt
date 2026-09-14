package com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze

import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.viewmodel.FileScannerViewModel
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.core.util.deprecated.SortMode
import com.example.androidmaiden.core.util.deprecated.SortOrder
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

/**
 * Stateful entry point for the File System Analysis feature.
 * Connects the UI to the FileScannerViewModel and manages top-level UI interaction state.
 *
 * @param onNavigateUp Callback for navigating back to the previous screen.
 */
@ExperimentalTime
@Composable
fun FileAnalysisPage(
    onNavigateUp: () -> Unit = {}
) {
    val vm: FileScannerViewModel = koinViewModel()

    // --- Interaction State ---
    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASC) }

    // --- ViewModel & Data ---
    val useMock = vm.useMock
    
    val root by remember(vm.currentDirectory) { mutableStateOf(vm.currentDirectory) } // Note: vm.currentDirectory is not a flow in the original code, but we assume it updates.
    // Re-reading original FileAnalysis.kt, vm.currentDirectory was accessed directly. 
    // In a real KMP app, this should be a Flow.
    
    val stats = vm.folderStats
    val isLoading by vm.isLoading.collectAsState()
    val loadError by vm.error.collectAsState()
    val pathStack = vm.pathStack

    // Start incremental sync when entering the screen to ensure real data is available
    LaunchedEffect(useMock) {
        if (!useMock) {
            vm.startSync()
        }
    }

    val windowSizeClass = LocalWindowSizeClass.current

    FileAnalysisCoordinator(
        windowSizeClass = windowSizeClass,
        root = vm.currentDirectory, // Accessing directly as in original
        stats = stats,
        isLoading = isLoading,
        loadError = loadError,
        pathStack = pathStack,
        useMock = useMock,
        viewMode = viewMode,
        sortMode = sortMode,
        sortOrder = sortOrder,
        onBack = {
            if (!vm.navigateBack()) {
                onNavigateUp()
            }
        },
        onNavigateTo = { vm.navigateTo(it) },
        onToggleSource = { vm.toggleSource() },
        onViewModeChange = { viewMode = it },
        onSortModeChange = { sortMode = it },
        onSortOrderChange = { sortOrder = it },
        onNavigateToStackIndex = { vm.navigateToStackIndex(it) },
        onDeleteNode = { vm.deleteNode(it) },
        onRenameNode = { node, newName -> vm.renameNode(node, newName) }
    )
}
