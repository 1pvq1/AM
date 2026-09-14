package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify

import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.viewmodel.PersistentFileViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

/**
 * Stateful entry point for the File Classification feature.
 * Connects the UI to the PersistentFileViewModel and manages top-level UI state.
 *
 * @param onBack Callback for navigating back to the previous screen.
 */
@ExperimentalTime
@Composable
fun FileClassifyPage(onBack: () -> Unit = {}) {
    val vm: PersistentFileViewModel = koinViewModel()

    // Observe state from the ViewModel
    val categories by vm.categories.collectAsState()
    val isSyncing by vm.isSyncing.collectAsState()
    val viewMode by vm.viewMode.collectAsState()
    val selectedCategory by vm.selectedCategory.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val searchResults by vm.searchResults.collectAsState()

    // Local UI state
    var isSearchActive by remember { mutableStateOf(false) }

    // Trigger incremental sync once on startup
    LaunchedEffect(Unit) {
        vm.startSync()
    }

    val windowSizeClass = LocalWindowSizeClass.current

    FileClassifyCoordinator(
        windowSizeClass = windowSizeClass,
        categories = categories,
        isSyncing = isSyncing,
        viewMode = viewMode,
        selectedCategory = selectedCategory,
        searchQuery = searchQuery,
        searchResults = searchResults,
        isSearchActive = isSearchActive,
        onBack = onBack,
        onSync = { vm.startSync() },
        onToggleView = {
            val nextMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
            vm.setViewMode(nextMode)
        },
        onSearchOpen = { isSearchActive = true },
        onSearchClose = {
            isSearchActive = false
            vm.updateSearchQuery("")
        },
        onSearchQueryChange = { vm.updateSearchQuery(it) },
        onCategorySelect = { vm.selectCategory(it) },
        onCategoryBack = { vm.selectCategory(null) },
        onDeleteFile = { vm.deleteFile(it.path) }
    )
}
