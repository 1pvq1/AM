package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.viewmodel.initialCategories
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

/**
 * Stateless screen content for the File Classification feature.
 * Supports split-pane canonical layout configurations on wide/tablet viewports.
 *
 * @param categories List of file categories to display.
 * @param isSyncing Whether a file scan is currently in progress.
 * @param viewMode The current view mode (List or Grid).
 * @param selectedCategory The currently selected category, if any.
 * @param searchQuery The current search query string.
 * @param searchResults List of file items matching the search query.
 * @param isSearchActive Whether the search interface is currently active.
 * @param isSplitPane Flag enforcing canonical list-detail layout presentation.
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
fun FileClassifyContent(
    categories: List<FileCategory>,
    isSyncing: Boolean,
    viewMode: ViewMode,
    selectedCategory: FileCategory?,
    searchQuery: String,
    searchResults: List<FileItem>,
    isSearchActive: Boolean,
    isSplitPane: Boolean,
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
    var previewFile by remember { mutableStateOf<FileItem?>(null) }

    if (isSplitPane) {
        // Canonical List-Detail Split-Pane for Large Screens/Tablets/Foldables
        Scaffold(
            topBar = {
                if (isSearchActive) {
                    SearchTopBar(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onClose = onSearchClose
                    )
                } else {
                    MainTopBar(
                        isSyncing = isSyncing,
                        viewMode = viewMode,
                        onBack = onBack,
                        onSync = onSync,
                        onToggleView = onToggleView,
                        onSearchOpen = onSearchOpen
                    )
                }
            }
        ) { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Master List Categories
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    if (viewMode == ViewMode.GRID) {
                        CategoryGridView(categories, onSelect = onCategorySelect)
                    } else {
                        CategoryListView(categories, onSelect = onCategorySelect)
                    }

                    if (isSyncing && categories.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                VerticalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                // Right Column: Detail Screen Viewport
                Box(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                    if (isSearchActive && searchQuery.length >= 2) {
                        SearchResultsView(searchResults) { file ->
                            previewFile = file
                        }
                    } else if (selectedCategory != null) {
                        FilesListPage(
                            categoryName = selectedCategory.name,
                            files = selectedCategory.files,
                            onBack = onCategoryBack,
                            onDelete = onDeleteFile
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Select a category to view files",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Standard full screen sequential navigation for compact mobile phones
        if (selectedCategory != null) {
            FilesListPage(
                categoryName = selectedCategory.name,
                files = selectedCategory.files,
                onBack = onCategoryBack,
                onDelete = onDeleteFile
            )
        } else {
            Scaffold(
                topBar = {
                    if (isSearchActive) {
                        SearchTopBar(
                            query = searchQuery,
                            onQueryChange = onSearchQueryChange,
                            onClose = onSearchClose
                        )
                    } else {
                        MainTopBar(
                            isSyncing = isSyncing,
                            viewMode = viewMode,
                            onBack = onBack,
                            onSync = onSync,
                            onToggleView = onToggleView,
                            onSearchOpen = onSearchOpen
                        )
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                    if (isSearchActive && searchQuery.length >= 2) {
                        SearchResultsView(searchResults) { file ->
                            previewFile = file
                        }
                    } else {
                        Column {
                            Box(modifier = Modifier.weight(1f)) {
                                if (viewMode == ViewMode.GRID) {
                                    CategoryGridView(categories, onSelect = onCategorySelect)
                                } else {
                                    CategoryListView(categories, onSelect = onCategorySelect)
                                }

                                if (isSyncing && categories.isEmpty()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Global Preview Overlay (for search results)
    previewFile?.let { file ->
        FilePreviewOverlay(
            file = file,
            onDismiss = { previewFile = null }
        )
    }
}

/**
 * Light theme preview for the classification content.
 */
@ExperimentalTime
@Preview
@Composable
fun FileClassifyContentLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileClassifyContent(
                categories = initialCategories,
                isSyncing = false,
                viewMode = ViewMode.LIST,
                selectedCategory = null,
                searchQuery = "",
                searchResults = emptyList(),
                isSearchActive = false,
                isSplitPane = false,
                onBack = {},
                onSync = {},
                onToggleView = {},
                onSearchOpen = {},
                onSearchClose = {},
                onSearchQueryChange = {},
                onCategorySelect = {},
                onCategoryBack = {},
                onDeleteFile = {}
            )
        }
    }
}

/**
 * Dark theme split-pane preview for large viewport sizes.
 */
@ExperimentalTime
@Preview
@Composable
fun FileClassifyContentSplitPaneDarkPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileClassifyContent(
                categories = initialCategories,
                isSyncing = false,
                viewMode = ViewMode.GRID,
                selectedCategory = null,
                searchQuery = "",
                searchResults = emptyList(),
                isSearchActive = false,
                isSplitPane = true,
                onBack = {},
                onSync = {},
                onToggleView = {},
                onSearchOpen = {},
                onSearchClose = {},
                onSearchQueryChange = {},
                onCategorySelect = {},
                onCategoryBack = {},
                onDeleteFile = {}
            )
        }
    }
}
