package com.example.androidmaiden.presentation.ui.screens.skills

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Adaptive Coordinator for the Skills screen.
 */
@Composable
fun SkillsAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
    features: List<Feature>,
    isTreeViewActive: Boolean,
    onTreeViewActiveChange: (Boolean) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val columns = when (windowSizeClass.widthCategory) {
        WindowSizeCategory.Compact -> 1
        WindowSizeCategory.Medium -> 2
        WindowSizeCategory.Expanded -> 3
    }

    SkillsContent(
        columns = columns,
        features = features,
        isTreeViewActive = isTreeViewActive,
        onTreeViewActiveChange = onTreeViewActiveChange,
        onNavigate = onNavigate
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun SkillsAdaptiveCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            SkillsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun SkillsAdaptiveCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            SkillsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun SkillsAdaptiveCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            SkillsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
        }
    }
}

@Composable
private fun SkillsAdaptiveCoordinatorPreviewHelper(widthCategory: WindowSizeCategory) {
    val sampleFeatures = listOf(
        Feature(1, "Skill 1", "Description 1", Icons.Default.Star),
        Feature(2, "Skill 2", "Description 2", Icons.Default.Settings),
        Feature(3, "Skill 3", "Description 3", Icons.Default.Person)
    )
    SkillsAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        features = sampleFeatures,
        isTreeViewActive = false,
        onTreeViewActiveChange = { },
        onNavigate = { }
    )
}

