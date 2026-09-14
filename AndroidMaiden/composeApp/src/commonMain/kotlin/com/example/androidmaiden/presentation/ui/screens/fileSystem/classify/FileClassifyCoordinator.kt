package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.FileCategory
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import kotlin.time.ExperimentalTime

/**
 * Adaptive Coordinator for the File Classification feature.
 * Currently, it delegates to FileClassifyContent, but it can be used to handle
 * different layout strategies for different window sizes in the future.
 *
 * @param windowSizeClass The current window size class of the device.
 * @param categories List of file categories to display.
 * @param isSyncing Whether a file scan is currently in progress.
 * @param viewMode The current view mode (List or Grid).
 * @param selectedCategory The currently selected category, if any.
 * @param searchQuery The current search query string.
 * @param searchResults List of file items matching the search query.
 * @param isSearchActive Whether the search interface is currently active.
 * @param onBack Callback for navigating back.
 * @param onSync Callback to trigger a manual file re-scan.
 * @param onToggleView Callback to switch between List and Grid view modes.
 * @param onSearchOpen Callback to open the search interface.
 * @param onSearchClose Callback to exit the search mode.
 * @param onSearchQueryChange Callback when the search query is updated.
 * @param onCategorySelect Callback when a category is selected.
 * @param onCategoryBack Callback to deselect the current category.
 * @param onDeleteFile Callback to delete a file.
 */
@ExperimentalTime
@Composable
fun FileClassifyCoordinator(
    windowSizeClass: WindowSizeClass,
    categories: List<FileCategory>,
    isSyncing: Boolean,
    viewMode: ViewMode,
    selectedCategory: FileCategory?,
    searchQuery: String,
    searchResults: List<FileItem>,
    isSearchActive: Boolean,
    onBack: () -> Unit,
    onSync: () -> Unit,
    onToggleView: () -> Unit,
    onSearchOpen: () -> Unit,
    onSearchClose: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (FileCategory) -> Unit,
    onCategoryBack: () -> Unit,
    onDeleteFile: (FileItem) -> Unit
) {
    // For now, classification is mostly a list/grid which works well across sizes.
    // In the future, we could show a split-pane for expanded screens here.
    FileClassifyContent(
        categories = categories,
        isSyncing = isSyncing,
        viewMode = viewMode,
        selectedCategory = selectedCategory,
        searchQuery = searchQuery,
        searchResults = searchResults,
        isSearchActive = isSearchActive,
        onBack = onBack,
        onSync = onSync,
        onToggleView = onToggleView,
        onSearchOpen = onSearchOpen,
        onSearchClose = onSearchClose,
        onSearchQueryChange = onSearchQueryChange,
        onCategorySelect = onCategorySelect,
        onCategoryBack = onCategoryBack,
        onDeleteFile = onDeleteFile
    )
}
