package com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview(name = "Compact", showBackground = true)
@Composable
fun FilesAdaptiveCoordinatorCompactPreview() {
    AppTheme {
        FilesAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
    }
}

@Preview(name = "Medium", showBackground = true)
@Composable
fun FilesAdaptiveCoordinatorMediumPreview() {
    AppTheme {
        FilesAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
    }
}

@Preview(name = "Expanded", showBackground = true)
@Composable
fun FilesAdaptiveCoordinatorExpandedPreview() {
    AppTheme {
        FilesAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
    }
}

@Composable
private fun FilesAdaptiveCoordinatorPreviewHelper(widthCategory: WindowSizeCategory) {
    val sampleFeatures = listOf(
        FileFeature("Photos", "Manage your photos", Icons.Default.Image),
        FileFeature("Documents", "Manage your docs", Icons.Default.Folder)
    )
    FilesAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        features = sampleFeatures,
        onNavigate = { }
    )
}
