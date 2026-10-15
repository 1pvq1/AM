package com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.StorageStats
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Adaptive Coordinator for the Files screen.
 *
 * @param windowSizeClass The current window size class.
 * @param features List of available file features.
 * @param storageStats Current storage usage statistics.
 * @param onNavigate Callback for navigation.
 */
@Composable
fun FilesAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
    features: List<FileFeature>,
    storageStats: StorageStats,
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
        storageStats = storageStats,
        onNavigate = onNavigate
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun FilesAdaptiveCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FilesAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun FilesAdaptiveCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FilesAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun FilesAdaptiveCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FilesAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
        }
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
        storageStats = StorageStats(),
        onNavigate = { }
    )
}

