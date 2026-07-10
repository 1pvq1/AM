package com.example.androidmaiden.presentation.ui.screens.skills

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.platform.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Data class representing a specific capability or feature of the app.
 */
data class Feature(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val screen: Screen? = null
)

/**
 * The Stateful Entry Point for the Skills screen.
 */
@Preview
@Composable
fun SkillsScreen(onNavigate: (Screen) -> Unit = {}) {
    // UI State
    var isTreeViewActive by remember { mutableStateOf(false) }

    // Feature list with icons
    val features = listOf(
        Feature(
            1,
            stringResource(id = "skills_task_mgmt_title"),
            stringResource(id = "skills_task_mgmt_desc"),
            Icons.Default.Checklist,
            Screen.Todo
        ),
        Feature(
            2,
            stringResource(id = "skills_char_interaction_title"),
            stringResource(id = "skills_char_interaction_desc"),
            Icons.Default.Forum,
            Screen.CharacterInteraction
        ),
        Feature(
            3,
            stringResource(id = "skills_theme_title"),
            stringResource(id = "skills_theme_desc"),
            Icons.Default.Palette,
            Screen.ThemeMatching
        ),
        Feature(
            4,
            stringResource(id = "skills_stats_title"),
            stringResource(id = "skills_stats_desc"),
            Icons.Default.BarChart
        ),
        Feature(
            5,
            stringResource(id = "skills_file_mgmt_title"),
            stringResource(id = "skills_file_mgmt_desc"),
            Icons.Default.Folder,
            Screen.Files
        ),
        Feature(
            6,
            stringResource(id = "skills_hw_mgmt_title"),
            stringResource(id = "skills_hw_mgmt_desc"),
            Icons.Default.Hardware,
            Screen.Hardware
        )
    )

    val windowSizeClass = LocalWindowSizeClass.current

    SkillsAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        features = features,
        isTreeViewActive = isTreeViewActive,
        onTreeViewActiveChange = { isTreeViewActive = it },
        onNavigate = onNavigate
    )
}
