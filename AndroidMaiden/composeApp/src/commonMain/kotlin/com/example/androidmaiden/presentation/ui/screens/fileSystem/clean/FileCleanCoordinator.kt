package com.example.androidmaiden.presentation.ui.screens.fileSystem.clean

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.CleanupStats
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass

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
