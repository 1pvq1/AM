package com.example.androidmaiden

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil3.compose.setSingletonImageLoaderFactory
import com.example.androidmaiden.model.Screen
import com.example.androidmaiden.screens.*
import com.example.androidmaiden.screens.fileSystem.analyze.FileAnalysisScreen
import com.example.androidmaiden.screens.fileSystem.classify.FileClassifyPage
import com.example.androidmaiden.screens.fileSystem.FilesScreen
import com.example.androidmaiden.screens.pages.*
import com.example.androidmaiden.screens.settings.llm.AdvancedLlmSettingsPage
import com.example.androidmaiden.ui.AppNavigationBar
import com.example.androidmaiden.utils.getAsyncImageLoader
import com.example.androidmaiden.viewModels.NavigationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun App() {
    KoinContext {
        val navViewModel: NavigationViewModel = koinViewModel()
        val currentScreen by navViewModel.currentScreen.collectAsState()
        val isNavigationBarVisible by navViewModel.isNavigationBarVisible.collectAsState()

        var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
        var buttonDisplayStyle by remember { mutableStateOf(ButtonDisplayStyle.ICON_ONLY) }

        val isDarkTheme = when (themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        CompositionLocalProvider(LocalButtonDisplayStyle provides buttonDisplayStyle) {
            MaterialTheme(colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()) {
                Scaffold(
                    bottomBar = {
                        if (isNavigationBarVisible) {
                            AppNavigationBar(
                                currentScreen = currentScreen,
                                onScreenSelected = { screen ->
                                    navViewModel.navigateTo(screen)
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (currentScreen) {
                            is Screen.Home -> HomeScreen()
                            is Screen.Settings -> SettingsScreen(
                                previewThemeMode = themeMode,
                                onThemePreview = { themeMode = it },
                                buttonDisplayStyle = buttonDisplayStyle,
                                onButtonDisplayStyleChange = { buttonDisplayStyle = it },
                                onNavigateToAdvancedLlmSettings = {
                                    navViewModel.navigateTo(Screen.AdvancedLlmSettings)
                                },
                                language = Language.FOLLOW_SYSTEM,
                                onLanguageChange = {}
                            )

                            is Screen.Skills -> SkillsPage(onNavigate = { screen ->
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
                                }
                            )

                            is Screen.Todo -> TodoPage()

                            is Screen.CharacterInteraction -> CharacterInteractionPage(
                                onFullScreenChange = { isFullScreen: Boolean ->
                                    navViewModel.setNavigationBarVisible(!isFullScreen)
                                },
                                onNavigateUp = {
                                    navViewModel.navigateTo(Screen.Home)
                                }
                            )

                            is Screen.AdvancedLlmSettings -> AdvancedLlmSettingsPage(onNavigateBack = {
                                navViewModel.navigateTo(Screen.Settings)
                            })
                        }
                    }
                }
            }
        }
    }
}
