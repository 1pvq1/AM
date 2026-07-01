package com.example.androidmaiden.presentation.ui.screens.themeMatching

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.viewmodel.ThemeMatchingViewModel
import com.example.androidmaiden.presentation.viewmodel.ThemeMatchingUiState
import com.example.androidmaiden.domain.themematching.model.CharacterAction
import com.example.androidmaiden.domain.themematching.model.CharacterStats
import com.example.androidmaiden.domain.themematching.model.AnimationConfig
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.screens.themeMatching.components.ActionLibraryCard
import com.example.androidmaiden.presentation.ui.screens.themeMatching.components.AnimationGuideCard
import com.example.androidmaiden.presentation.ui.screens.themeMatching.components.LivePreviewCard
import com.example.androidmaiden.presentation.ui.screens.themeMatching.components.TableLayoutPreviewCard
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * The Stateful Entry Point for the Theme Matching Screen.
 * Cohesively coordinates vertical layout lists for compact phone views
 * and horizontal split grid views for larger tablet or desktop monitors.
 *
 * @param onBack Callback interface invoked to exit this screen.
 * @param viewModel ViewModel holding active states and actions, injected via Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeMatchingScreen(
    onBack: () -> Unit,
    viewModel: ThemeMatchingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ThemeMatchingContent(
        uiState = uiState,
        onBack = onBack,
        onToggleGuide = { viewModel.toggleGuide() },
        onActionSelected = { viewModel.selectAction(it) },
        onPlayPauseToggle = { viewModel.togglePlayback() },
        onSpeedScaleChange = { viewModel.updateSpeedScale(it) },
        onTabSelected = { viewModel.selectTableTab(it) }
    )
}

/**
 * The Stateless Content for the Theme Matching Screen.
 * Useful for Previews and testing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeMatchingContent(
    uiState: ThemeMatchingUiState,
    onBack: () -> Unit,
    onToggleGuide: () -> Unit,
    onActionSelected: (CharacterAction) -> Unit,
    onPlayPauseToggle: () -> Unit,
    onSpeedScaleChange: (Float) -> Unit,
    onTabSelected: (Int) -> Unit
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val isWideScreen = windowSizeClass.widthCategory != WindowSizeCategory.Compact

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Matching & Character Animation") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isWideScreen) {
                // Horizontal split for wide screens (tablets, desktop)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimationGuideCard(
                            expanded = uiState.showGuide,
                            onToggle = onToggleGuide
                        )

                        ActionLibraryCard(
                            activeAction = uiState.activeAction,
                            onActionSelected = onActionSelected,
                            isPlaying = uiState.isPlaying,
                            onPlayPauseToggle = onPlayPauseToggle,
                            speedScale = uiState.speedScale,
                            onSpeedScaleChange = onSpeedScaleChange
                        )

                        TableLayoutPreviewCard(
                            selectedTab = uiState.selectedTableTab,
                            onTabSelected = onTabSelected,
                            activeAction = uiState.activeAction,
                            stats = uiState.stats,
                            configs = uiState.configs
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Live Interactive Preview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LivePreviewCard(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            activeAction = uiState.activeAction,
                            isPlaying = uiState.isPlaying,
                            speedScale = uiState.speedScale
                        )
                    }
                }
            } else {
                // Vertical layout for phone screens
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LivePreviewCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        activeAction = uiState.activeAction,
                        isPlaying = uiState.isPlaying,
                        speedScale = uiState.speedScale
                    )

                    AnimationGuideCard(
                        expanded = uiState.showGuide,
                        onToggle = onToggleGuide
                    )

                    ActionLibraryCard(
                        activeAction = uiState.activeAction,
                        onActionSelected = onActionSelected,
                        isPlaying = uiState.isPlaying,
                        onPlayPauseToggle = onPlayPauseToggle,
                        speedScale = uiState.speedScale,
                        onSpeedScaleChange = onSpeedScaleChange
                    )

                    TableLayoutPreviewCard(
                        selectedTab = uiState.selectedTableTab,
                        onTabSelected = onTabSelected,
                        activeAction = uiState.activeAction,
                        stats = uiState.stats,
                        configs = uiState.configs
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ThemeMatchingPreview() {
    AppTheme {
        ThemeMatchingContent(
            uiState = ThemeMatchingUiState(
                actions = CharacterAction.entries,
                stats = listOf(
                    CharacterStats("Health", "100", "+5"),
                    CharacterStats("Mana", "50", "+2")
                ),
                configs = listOf(
                    AnimationConfig(CharacterAction.IDLE, "1000", true, "Linear"),
                    AnimationConfig(CharacterAction.WALKING, "800", true, "EaseIn")
                )
            ),
            onBack = {},
            onToggleGuide = {},
            onActionSelected = {},
            onPlayPauseToggle = {},
            onSpeedScaleChange = {},
            onTabSelected = {}
        )
    }
}
