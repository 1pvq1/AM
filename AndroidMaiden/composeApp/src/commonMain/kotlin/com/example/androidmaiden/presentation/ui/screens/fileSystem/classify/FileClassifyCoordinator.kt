package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.FileCategory
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.model.StorageLocationInfo
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.initialCategories
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

/**
 * Adaptive Coordinator for the File Classification feature.
 * Delegates to FileClassifyContent and handles viewport scoping across device form factors.
 *
 * @param windowSizeClass The current window size class of the device.
 * @param categories List of file categories to display.
 * @param isSyncing Whether a file scan is currently in progress.
 * @param viewMode The current view mode (List or Grid).
 * @param selectedCategory The currently selected category, if any.
 * @param searchQuery The current search query string.
 * @param searchResults List of file items matching the search query.
 * @param isSearchActive Whether the search interface is currently active.
 * @param storageLocations List of detected storage locations.
 * @param selectedStoragePath Actively selected storage path.
 * @param storageLocationInfo Fallback storage location metrics model.
 * @param onBack Callback for navigating back.
 * @param onSync Callback to trigger a manual file re-scan.
 * @param onToggleView Callback to switch between List and Grid view modes.
 * @param onSearchOpen Callback to open the search interface.
 * @param onSearchClose Callback to exit the search mode.
 * @param onSearchQueryChange Callback when the search query is updated.
 * @param onCategorySelect Callback when a category is selected.
 * @param onCategoryBack Callback to deselect the current category.
 * @param onStorageSelect Callback when a storage location is selected.
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
    val isSplitPane = windowSizeClass.widthCategory != WindowSizeCategory.Compact

    FileClassifyContent(
        categories = categories,
        isSyncing = isSyncing,
        viewMode = viewMode,
        selectedCategory = selectedCategory,
        searchQuery = searchQuery,
        searchResults = searchResults,
        isSearchActive = isSearchActive,
        isSplitPane = isSplitPane,
        storageLocations = storageLocations,
        selectedStoragePath = selectedStoragePath,
        storageLocationInfo = storageLocationInfo,
        onBack = onBack,
        onSync = onSync,
        onToggleView = onToggleView,
        onSearchOpen = onSearchOpen,
        onSearchClose = onSearchClose,
        onSearchQueryChange = onSearchQueryChange,
        onCategorySelect = onCategorySelect,
        onCategoryBack = onCategoryBack,
        onStorageSelect = onStorageSelect,
        onDeleteFile = onDeleteFile
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

private val sampleInternalStorage = StorageLocationInfo(
    name = "Primary Internal Storage",
    path = "/storage/emulated/0",
    isExternalRemovable = false,
    totalBytes = 128_000_000_000L,
    usedBytes = 42_000_000_000L,
    freeBytes = 86_000_000_000L
)

private val sampleSdCardStorage = StorageLocationInfo(
    name = "External SD Card Storage",
    path = "/storage/sdcard1",
    isExternalRemovable = true,
    totalBytes = 64_000_000_000L,
    usedBytes = 32_000_000_000L,
    freeBytes = 32_000_000_000L
)

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun FileClassifyCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileClassifyCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Compact,
                    heightCategory = WindowSizeCategory.Medium
                ),
                categories = initialCategories,
                isSyncing = false,
                viewMode = ViewMode.GRID,
                selectedCategory = null,
                searchQuery = "",
                searchResults = emptyList(),
                isSearchActive = false,
                storageLocations = listOf(sampleInternalStorage, sampleSdCardStorage),
                selectedStoragePath = sampleInternalStorage.path,
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
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun FileClassifyCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileClassifyCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Medium,
                    heightCategory = WindowSizeCategory.Medium
                ),
                categories = initialCategories,
                isSyncing = false,
                viewMode = ViewMode.GRID,
                selectedCategory = null,
                searchQuery = "",
                searchResults = emptyList(),
                isSearchActive = false,
                storageLocations = listOf(sampleInternalStorage, sampleSdCardStorage),
                selectedStoragePath = sampleSdCardStorage.path,
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
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun FileClassifyCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileClassifyCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Expanded,
                    heightCategory = WindowSizeCategory.Expanded
                ),
                categories = initialCategories,
                isSyncing = false,
                viewMode = ViewMode.GRID,
                selectedCategory = null,
                searchQuery = "",
                searchResults = emptyList(),
                isSearchActive = false,
                storageLocations = listOf(sampleInternalStorage, sampleSdCardStorage),
                selectedStoragePath = sampleInternalStorage.path,
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
