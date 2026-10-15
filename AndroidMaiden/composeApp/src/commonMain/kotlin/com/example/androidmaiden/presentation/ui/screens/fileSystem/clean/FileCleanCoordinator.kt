package com.example.androidmaiden.presentation.ui.screens.fileSystem.clean

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.CleanupStats
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.domain.model.TrashRecord
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

/**
 * Adaptive Coordinator for the File Cleanup feature.
 *
 * @param windowSizeClass The current window size class of the device.
 * @param stats The cleanup statistics.
 * @param onBack Callback for navigating back.
 * @param onClearDuplicates Callback to trigger clearing duplicate files.
 * @param onClearEmptyFolders Callback to trigger clearing empty folders.
 * @param onRestoreFromTrash Callback to restore a file from the recycle bin.
 * @param onDeletePermanently Callback to delete a file permanently from the recycle bin.
 * @param snackbarHostState The snackbar host state for displaying messages.
 */
@Composable
fun FileCleanCoordinator(
    windowSizeClass: WindowSizeClass,
    stats: CleanupStats,
    onBack: () -> Unit,
    onClearDuplicates: () -> Unit,
    onClearEmptyFolders: () -> Unit,
    onRestoreFromTrash: (String) -> Unit,
    onDeletePermanently: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    // Standard cleanup view is suitable for most sizes.
    // Future enhancements for large screens could include a side-by-side view for duplicates.
    FileCleanContent(
        stats = stats,
        onBack = onBack,
        onClearDuplicates = onClearDuplicates,
        onClearEmptyFolders = onClearEmptyFolders,
        onRestoreFromTrash = onRestoreFromTrash,
        onDeletePermanently = onDeletePermanently,
        snackbarHostState = snackbarHostState
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

@OptIn(ExperimentalTime::class)
private val sampleCleanupStats = CleanupStats(
    duplicateFiles = listOf(
        FileItem(
            path = "/storage/emulated/0/DCIM/photo_dup_1.jpg",
            name = "photo_dup_1.jpg",
            isDirectory = false,
            lastModified = 0L,
            size = 4_200_000L,
            parentPath = "/storage/emulated/0/DCIM",
            mimeType = "image/jpeg"
        ),
        FileItem(
            path = "/storage/emulated/0/DCIM/Camera/photo_dup_2.jpg",
            name = "photo_dup_2.jpg",
            isDirectory = false,
            lastModified = 0L,
            size = 4_200_000L,
            parentPath = "/storage/emulated/0/DCIM/Camera",
            mimeType = "image/jpeg"
        )
    ),
    emptyFolders = listOf(
        FileItem(
            path = "/storage/emulated/0/EmptyDir",
            name = "EmptyDir",
            isDirectory = true,
            lastModified = 0L,
            size = 0L,
            parentPath = "/storage/emulated/0",
            mimeType = ""
        )
    ),
    trashEntries = listOf(
        TrashRecord(
            originalPath = "/storage/emulated/0/Download/old_doc.pdf",
            trashPath = "/trash/old_doc.pdf",
            deletedAt = 0L,
            fileName = "old_doc.pdf",
            size = 1_500_000L,
            isDirectory = false
        )
    )
)

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun FileCleanCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileCleanCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Compact,
                    heightCategory = WindowSizeCategory.Medium
                ),
                stats = sampleCleanupStats,
                onBack = {},
                onClearDuplicates = {},
                onClearEmptyFolders = {},
                onRestoreFromTrash = {},
                onDeletePermanently = {},
                snackbarHostState = SnackbarHostState()
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
fun FileCleanCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileCleanCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Medium,
                    heightCategory = WindowSizeCategory.Medium
                ),
                stats = sampleCleanupStats,
                onBack = {},
                onClearDuplicates = {},
                onClearEmptyFolders = {},
                onRestoreFromTrash = {},
                onDeletePermanently = {},
                snackbarHostState = SnackbarHostState()
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
fun FileCleanCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileCleanCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Expanded,
                    heightCategory = WindowSizeCategory.Expanded
                ),
                stats = sampleCleanupStats,
                onBack = {},
                onClearDuplicates = {},
                onClearEmptyFolders = {},
                onRestoreFromTrash = {},
                onDeletePermanently = {},
                snackbarHostState = SnackbarHostState()
            )
        }
    }
}

