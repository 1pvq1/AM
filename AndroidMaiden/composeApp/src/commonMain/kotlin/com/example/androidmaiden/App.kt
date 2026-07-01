package com.example.androidmaiden

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil3.compose.setSingletonImageLoaderFactory
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.screens.home.*
import com.example.androidmaiden.presentation.ui.screens.skills.*
import com.example.androidmaiden.presentation.ui.screens.settings.*
import com.example.androidmaiden.presentation.ui.screens.todo.TodoScreen
import com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard.FilesScreen
import com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.clean.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.organize.*
import com.example.androidmaiden.presentation.ui.screens.interaction.*
import com.example.androidmaiden.presentation.ui.screens.settings.llm.*
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.presentation.ui.theme.*
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.screens.themeMatching.ThemeMatchingScreen
import com.example.androidmaiden.util.*
import com.example.androidmaiden.presentation.viewmodel.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

/**
 * The main entry point for the Compose Multiplatform application.
 */
@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun App() {
    KoinContext {
        val navViewModel = koinViewModel<NavigationViewModel>()
        val currentScreen by navViewModel.currentScreen.collectAsState()
        val isNavigationBarVisible by navViewModel.isNavigationBarVisible.collectAsState()

        val settingsViewModel = koinViewModel<SettingsViewModel>()
        val themeMode by settingsViewModel.themeMode.collectAsState()
        val themeType by settingsViewModel.themeType.collectAsState()
        val useDynamicColor by settingsViewModel.useDynamicColor.collectAsState()
        val buttonDisplayStyle by settingsViewModel.buttonDisplayStyle.collectAsState()

        val windowSizeClass = rememberWindowSizeClass()
        var manualWidthCategory by remember { mutableStateOf<WindowSizeCategory?>(null) }
        
        val effectiveWindowSizeClass = remember(windowSizeClass, manualWidthCategory) {
            if (manualWidthCategory != null && windowSizeClass.widthCategory != WindowSizeCategory.Compact) {
                windowSizeClass.copy(widthCategory = manualWidthCategory!!)
            } else {
                windowSizeClass
            }
        }

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        CompositionLocalProvider(LocalWindowSizeClass provides effectiveWindowSizeClass) {
            AppTheme(
                themeType = themeType,
                themeMode = themeMode,
                useDynamicColor = useDynamicColor,
                buttonDisplayStyle = buttonDisplayStyle
            ) {
                val showNavigationRail = effectiveWindowSizeClass.widthCategory != WindowSizeCategory.Compact
                
                Row(modifier = Modifier.fillMaxSize()) {
                    if (showNavigationRail && isNavigationBarVisible) {
                        AppNavigationRail(
                            currentScreen = currentScreen,
                            onScreenSelected = { screen -> navViewModel.navigateTo(screen) },
                            windowSizeClass = effectiveWindowSizeClass,
                            onToggleLayout = {
                                manualWidthCategory = if (effectiveWindowSizeClass.widthCategory == WindowSizeCategory.Expanded) {
                                    WindowSizeCategory.Medium
                                } else {
                                    WindowSizeCategory.Expanded
                                }
                            }
                        )
                    }
                    
                    Scaffold(
                        bottomBar = {
                            if (!showNavigationRail && isNavigationBarVisible) {
                                AppNavigationBar(
                                    currentScreen = currentScreen,
                                    onScreenSelected = { screen -> navViewModel.navigateTo(screen) }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                            AppScreenContent(
                                currentScreen = currentScreen,
                                navViewModel = navViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun AppScreenContent(
    currentScreen: Screen,
    navViewModel: NavigationViewModel
) {
    when (currentScreen) {
        is Screen.Home -> HomeScreen()
        is Screen.Settings -> SettingsScreen(
            onNavigateToAdvancedLlmSettings = {
                navViewModel.navigateTo(Screen.AdvancedLlmSettings)
            }
        )

        is Screen.Skills -> SkillsScreen(onNavigate = { screen ->
            navViewModel.navigateTo(screen)
        })

        is Screen.Files -> FilesScreen(onNavigate = { screen ->
            navViewModel.navigateTo(screen)
        })

        is Screen.FileAnalysis -> FileAnalysisScreen(onNavigateUp = {
            navViewModel.navigateTo(Screen.Files)
        })

        is Screen.FileClassify -> FileClassifyPage(
            onBack = {
                navViewModel.navigateTo(Screen.Files)
            })

        is Screen.FileOrganize -> FileOrganizePage(
            onBack = {
                navViewModel.navigateTo(Screen.Files)
            })

        is Screen.FileClean -> FileCleanPage(
            onBack = {
                navViewModel.navigateTo(Screen.Files)
            })

        is Screen.Todo -> {
            val todoViewModel: TodoViewModel = koinViewModel()
            TodoScreen(viewModel = todoViewModel)
        }

        is Screen.CharacterInteraction -> CharacterInteractionScreen(
            onFullScreenChange = { isFullScreen: Boolean ->
                navViewModel.setNavigationBarVisible(!isFullScreen)
            }
        )

        is Screen.AdvancedLlmSettings -> AdvancedLlmSettingsPage(onNavigateBack = {
            navViewModel.navigateTo(Screen.Settings)
        })

        is Screen.ThemeMatching -> ThemeMatchingScreen(onBack = {
            navViewModel.navigateTo(Screen.Skills)
        })
    }
}
