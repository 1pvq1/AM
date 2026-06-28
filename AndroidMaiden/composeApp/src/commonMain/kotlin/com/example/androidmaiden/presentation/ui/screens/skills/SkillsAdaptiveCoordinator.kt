package com.example.androidmaiden.presentation.ui.screens.skills

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.adaptive.*

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
