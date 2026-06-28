package com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.adaptive.*

/**
 * The Adaptive Coordinator for the Files screen.
 */
@Composable
fun FilesAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
    features: List<FileFeature>,
    onNavigate: (Screen) -> Unit
) {
    val columns = when (windowSizeClass.widthCategory) {
        WindowSizeCategory.Compact -> 1
        WindowSizeCategory.Medium -> 2
        WindowSizeCategory.Expanded -> 2
    }

    FilesContent(
        columns = columns,
        features = features,
        onNavigate = onNavigate
    )
}
