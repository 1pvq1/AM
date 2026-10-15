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
 * Prioritizes mobile devices and smartphone screen layouts.
 *
 * @param categories List of file categories to display.
 * @param isSyncing Whether a file scan is currently in progress.
 * @param viewMode The current view mode (List or Grid). Default is Grid.
 * @param selectedCategory The currently selected category, if any.
 * @param searchQuery The current search query string.
 * @param searchResults List of file items matching the search query.
 * @param isSearchActive Whether the search interface is currently active.
 * @param isSplitPane Flag enforcing canonical list-detail layout presentation.
 * @param storageLocations List of detected storage locations (Internal Storage, SD Card, etc.).
 * @param selectedStoragePath Currently selected storage location path.
 * @param storageLocationInfo Fallback single storage location metric model.
 * @param onBack Callback for navigating back.
 * @param onSync Callback to trigger a manual file re-scan.
 * @param onToggleView Callback to switch between List and Grid view modes.
 * @param onSearchOpen Callback to open the search interface.
 * @param onSearchClose Callback to exit the search mode.
 * @param onSearchQueryChange Callback when the search query is updated.
 * @param onCategorySelect Callback when a category is selected.
 * @param onCategoryBack Callback to deselect the current category.
 * @param onStorageSelect Callback when a storage location volume card is selected.
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
    storageLocations: List<StorageLocationInfo> = emptyList(),
    selectedStoragePath: String? = null,
    storageLocationInfo: StorageLocationInfo? = null,
    onBack: () -> Unit,
    onSync: () -> Unit,
    onToggleView: () -> Unit,
    onSearchOpen: () -> Unit,
    onSearchClose: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (FileCategory) -> Unit,
    onCategoryBack: () -> Unit,
    onStorageSelect: (StorageLocationInfo) -> Unit = {},
    onDeleteFile: (FileItem) -> Unit
) {
    var previewFile by remember { mutableStateOf<FileItem?>(null) }

    val effectiveStorageLocations = remember(storageLocations, storageLocationInfo) {
        if (storageLocations.isNotEmpty()) {
            storageLocations
        } else if (storageLocationInfo != null) {
            listOf(storageLocationInfo)
        } else {
            emptyList()
        }
    }

    val headerComposable: (@Composable () -> Unit)? = remember(
        effectiveStorageLocations,
        selectedStoragePath
    ) {
        if (effectiveStorageLocations.isNotEmpty()) {
            {
                StorageLocationsSection(
                    locations = effectiveStorageLocations,
                    selectedPath = selectedStoragePath,
                    isCompactWidth = true,
                    onStorageSelect = onStorageSelect
                )
            }
        } else null
    }

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
                        CategoryGridView(categories, onSelect = onCategorySelect, headerContent = headerComposable)
                    } else {
                        CategoryListView(categories, onSelect = onCategorySelect, headerContent = headerComposable)
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
                    if ((isSearchActive) && (searchQuery.length >= 2)) {
                        SearchResultsView(searchResults) { file ->
                            previewFile = file
                        }
                    } else if (selectedCategory != null) {
                        FilesListPage(
                            categoryName = selectedCategory.name,
                            files = selectedCategory.files,
                            subcategories = selectedCategory.subcategories,
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
                subcategories = selectedCategory.subcategories,
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
                    if ((isSearchActive) && (searchQuery.length >= 2)) {
                        SearchResultsView(searchResults) { file ->
                            previewFile = file
                        }
                    } else {
                        Column {
                            Box(modifier = Modifier.weight(1f)) {
                                if (viewMode == ViewMode.GRID) {
                                    CategoryGridView(categories, onSelect = onCategorySelect, headerContent = headerComposable)
                                } else {
                                    CategoryListView(categories, onSelect = onCategorySelect, headerContent = headerComposable)
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
 * Light theme smartphone preview for the file classification content.
 */
@ExperimentalTime
@Preview
@Composable
fun FileClassifyContentLightPreview() {
    val internalStorage = StorageLocationInfo(
        name = "Primary Internal Storage",
        path = "/storage/emulated/0",
        isExternalRemovable = false,
        totalBytes = 128_000_000_000L,
        usedBytes = 42_000_000_000L,
        freeBytes = 86_000_000_000L
    )
    val sdCardStorage = StorageLocationInfo(
        name = "External SD Card Storage",
        path = "/storage/sdcard1",
        isExternalRemovable = true,
        totalBytes = 64_000_000_000L,
        usedBytes = 20_000_000_000L,
        freeBytes = 44_000_000_000L
    )

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
                viewMode = ViewMode.GRID,
                selectedCategory = null,
                searchQuery = "",
                searchResults = emptyList(),
                isSearchActive = false,
                isSplitPane = false,
                storageLocations = listOf(internalStorage, sdCardStorage),
                selectedStoragePath = internalStorage.path,
                onBack = {},
                onSync = {},
                onToggleView = {},
                onSearchOpen = {},
                onSearchClose = {},
                onSearchQueryChange = {},
                onCategorySelect = {},
                onCategoryBack = {},
                onStorageSelect = {},
                onDeleteFile = {}
            )
        }
    }
}

/**
 * Dark theme smartphone preview for the file classification content.
 */
@ExperimentalTime
@Preview
@Composable
fun FileClassifyContentDarkPreview() {
    val internalStorage = StorageLocationInfo(
        name = "Primary Internal Storage",
        path = "/storage/emulated/0",
        isExternalRemovable = false,
        totalBytes = 128_000_000_000L,
        usedBytes = 42_000_000_000L,
        freeBytes = 86_000_000_000L
    )
    val sdCardStorage = StorageLocationInfo(
        name = "External SD Card Storage",
        path = "/storage/sdcard1",
        isExternalRemovable = true,
        totalBytes = 64_000_000_000L,
        usedBytes = 32_000_000_000L,
        freeBytes = 32_000_000_000L
    )

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
                isSplitPane = false,
                storageLocations = listOf(internalStorage, sdCardStorage),
                selectedStoragePath = sdCardStorage.path,
                onBack = {},
                onSync = {},
                onToggleView = {},
                onSearchOpen = {},
                onSearchClose = {},
                onSearchQueryChange = {},
                onCategorySelect = {},
                onCategoryBack = {},
                onStorageSelect = {},
                onDeleteFile = {}
            )
        }
    }
}
