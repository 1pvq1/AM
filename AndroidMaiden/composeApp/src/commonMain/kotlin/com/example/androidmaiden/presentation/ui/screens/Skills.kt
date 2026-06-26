package com.example.androidmaiden.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.presentation.ui.features.others.DraftSkillTreeV2
import com.example.androidmaiden.presentation.ui.adaptive.*
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

    var isTreeViewActive by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Toolbar with view switching buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = "skills_title"),
                style = MaterialTheme.typography.headlineMedium
            )
            
            // View Mode Toggle Icons (List vs Tree)
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                IconButton(
                    onClick = { isTreeViewActive = false },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (!isTreeViewActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (!isTreeViewActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "List View",
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { isTreeViewActive = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (isTreeViewActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (isTreeViewActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountTree,
                        contentDescription = "Interactive Skill Tree",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (!isTreeViewActive) {
                // Classic card list/grid view (Default View)
                val windowSize = LocalWindowSizeClass.current
                val columns = when (windowSize.widthCategory) {
                    WindowSizeCategory.Compact -> 1
                    WindowSizeCategory.Medium -> 2
                    WindowSizeCategory.Expanded -> 3
                }

                if (columns == 1) {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        features.forEach { feature ->
                            FeatureCard(feature) {
                                feature.screen?.let { onNavigate(it) }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(features) { feature ->
                            FeatureCard(feature) {
                                feature.screen?.let { onNavigate(it) }
                            }
                        }
                    }
                }
            } else {
                // RPG Skill Tree Interactive View V2
                DraftSkillTreeV2(onNavigate = onNavigate)
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
