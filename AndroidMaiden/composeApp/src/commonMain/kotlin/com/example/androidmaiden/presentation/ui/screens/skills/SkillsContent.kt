package com.example.androidmaiden.presentation.ui.screens.skills

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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.presentation.ui.features.others.DraftSkillTreeV2
import com.example.androidmaiden.platform.stringResource

/**
 * The Stateless UI for the Skills screen.
 */
@Composable
fun SkillsContent(
    columns: Int,
    features: List<Feature>,
    isTreeViewActive: Boolean,
    onTreeViewActiveChange: (Boolean) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Toolbar
        SkillsTopBar(
            isTreeViewActive = isTreeViewActive,
            onTreeViewActiveChange = onTreeViewActiveChange
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (!isTreeViewActive) {
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
                DraftSkillTreeV2(onNavigate = onNavigate)
            }
        }
    }
}

@Composable
private fun SkillsTopBar(
    isTreeViewActive: Boolean,
    onTreeViewActiveChange: (Boolean) -> Unit
) {
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

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            IconButton(
                onClick = { onTreeViewActiveChange(false) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (!isTreeViewActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (!isTreeViewActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "List View",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = { onTreeViewActiveChange(true) },
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
