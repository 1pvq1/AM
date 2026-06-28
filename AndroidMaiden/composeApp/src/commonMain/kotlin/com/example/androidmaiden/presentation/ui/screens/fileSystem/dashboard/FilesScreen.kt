package com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.platform.RequestStoragePermission
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateful Entry Point for the Files screen.
 */
@Composable
fun FilesScreen(onNavigate: (Screen) -> Unit) {
    val features = listOf(
        FileFeature(stringResource(id = "analyze"), stringResource(id = "analyze_description"), Icons.Default.Analytics, Screen.FileAnalysis),
        FileFeature(stringResource(id = "classify"), stringResource(id = "classify_description"), Icons.Default.Style, Screen.FileClassify),
        FileFeature(stringResource(id = "organize"), stringResource(id = "organize_description"), Icons.Default.CreateNewFolder, Screen.FileOrganize),
        FileFeature(stringResource(id = "clean"), stringResource(id = "clean_description"), Icons.Default.CleaningServices, Screen.FileClean)
    )

    // Request storage permission
    RequestStoragePermission()

    val windowSizeClass = LocalWindowSizeClass.current

    FilesAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        features = features,
        onNavigate = onNavigate
    )
}

@Preview
@Composable
fun FileSysScreenPreview(){
    FilesScreen {  }
}