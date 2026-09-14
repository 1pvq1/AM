package com.example.androidmaiden.presentation.ui.screens.fileSystem.clean

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.domain.model.CleanupResult
import com.example.androidmaiden.presentation.viewmodel.FileClearViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Stateful entry point for the File Cleanup feature.
 * Connects the UI to the FileClearViewModel and manages cleanup notifications.
 *
 * @param onBack Callback for navigating back to the previous screen.
 */
@Composable
fun FileCleanPage(onBack: () -> Unit) {
    val vm: FileClearViewModel = koinViewModel()
    val stats by vm.cleanupStats.collectAsState()
    val result by vm.cleanupResult.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(result) {
        result?.let {
            when (it) {
                is CleanupResult.Success -> {
                    snackbarHostState.showSnackbar("Cleanup successful")
                    vm.resetResult()
                }
                is CleanupResult.Error -> {
                    snackbarHostState.showSnackbar("Error: ${it.message}")
                    vm.resetResult()
                }
                is CleanupResult.Loading -> {}
            }
        }
    }

    val windowSizeClass = LocalWindowSizeClass.current

    FileCleanCoordinator(
        windowSizeClass = windowSizeClass,
        stats = stats,
        onBack = onBack,
        onClearDuplicates = { vm.clearDuplicates() },
        onClearEmptyFolders = { vm.clearEmptyFolders() },
        onRestoreFromTrash = { vm.restoreFromTrash(it) },
        onDeletePermanently = { vm.deletePermanently(it) },
        snackbarHostState = snackbarHostState
    )
}
