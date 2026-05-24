package com.example.androidmaiden.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.platform.*
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
 * The Skills screen showcasing the various features of the application.
 */
@Preview
@Composable
fun SkillsPage(onNavigate: (Screen) -> Unit = {}) {
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
            Icons.Default.Palette
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
            Icons.Default.Hardware
        )
    )
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = "skills_title"),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Render feature cards
        features.forEach { feature ->
            FeatureCard(feature) {
                feature.screen?.let { onNavigate(it) }
            }
        }
    }
}

/**
 * Composable for displaying a feature as a card.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    val isClickable = feature.screen != null
    BaseCard(
        title = feature.title,
        description = feature.description,
        icon = feature.icon,
        onClick = onClick,
        isClickable = isClickable,
        trailingIcon = if (isClickable) Icons.AutoMirrored.Filled.ArrowForward else null
    )
}
