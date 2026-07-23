package com.example.androidmaiden.presentation.ui.screens.skills

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
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

@Preview(name = "Compact", showBackground = true)
@Composable
fun SkillsAdaptiveCoordinatorCompactPreview() {
    AppTheme {
        SkillsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
    }
}

@Preview(name = "Medium", showBackground = true)
@Composable
fun SkillsAdaptiveCoordinatorMediumPreview() {
    AppTheme {
        SkillsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
    }
}

@Preview(name = "Expanded", showBackground = true)
@Composable
fun SkillsAdaptiveCoordinatorExpandedPreview() {
    AppTheme {
        SkillsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
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
