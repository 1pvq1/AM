package com.example.androidmaiden.presentation.ui.screens.themeMatching.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.themematching.model.CharacterAction
import com.example.androidmaiden.domain.themematching.model.CharacterStats
import com.example.androidmaiden.domain.themematching.model.AnimationConfig
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview panel of various table layouts.
 * Provides three tabs showing different table layout styles.
 *
 * @param selectedTab Index of the active tab.
 * @param onTabSelected Invoked when a tab header is clicked.
 * @param activeAction The active CharacterAction (used to highlight rows in config table).
 * @param stats The list of character statistics retrieved from domain layer.
 * @param configs The list of animation configurations retrieved from domain layer.
 */
@Composable
fun TableLayoutPreviewCard(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    activeAction: CharacterAction,
    stats: List<CharacterStats>,
    configs: List<AnimationConfig>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Table Layout Previews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    text = { Text("Config Data") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    text = { Text("Attributes") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { onTabSelected(2) },
                    text = { Text("Theme Palette") }
                )
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                0 -> AnimationConfigTable(activeAction, configs)
                1 -> CharacterStatsTable(stats)
                2 -> ColorPaletteTable()
            }
        }
    }
}

@Preview
@Composable
fun TableLayoutPreviewCardPreview() {
    AppTheme {
        TableLayoutPreviewCard(
            selectedTab = 0,
            onTabSelected = {},
            activeAction = CharacterAction.IDLE,
            stats = listOf(
                CharacterStats("Health", "100", "+5"),
                CharacterStats("Mana", "50", "+2")
            ),
            configs = listOf(
                AnimationConfig(CharacterAction.IDLE, "1000", true, "Linear"),
                AnimationConfig(CharacterAction.WALKING, "800", true, "EaseIn")
            )
        )
    }
}
