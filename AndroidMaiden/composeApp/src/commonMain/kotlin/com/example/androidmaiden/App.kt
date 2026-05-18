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
import com.example.androidmaiden.screens.fileSystem.FilesScreen
import com.example.androidmaiden.screens.fileSystem.analyze.FileAnalysisScreen
import com.example.androidmaiden.screens.fileSystem.classify.FileClassifyPage
import com.example.androidmaiden.screens.fileSystem.clean.FileCleanPage
import com.example.androidmaiden.screens.fileSystem.organize.FileOrganizePage
import com.example.androidmaiden.screens.pages.*
import com.example.androidmaiden.screens.settings.llm.AdvancedLlmSettingsPage
import com.example.androidmaiden.ui.AppNavigationBar
import com.example.androidmaiden.ui.theme.AppTheme
import com.example.androidmaiden.ui.theme.core.AppThemeType
import com.example.androidmaiden.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.ui.theme.core.ThemeMode
import com.example.androidmaiden.utils.getAsyncImageLoader
import com.example.androidmaiden.viewModels.NavigationViewModel
import com.example.androidmaiden.viewModels.SettingsViewModel
import com.example.androidmaiden.viewModels.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

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

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        AppTheme(
            themeType = themeType,
            themeMode = themeMode,
            useDynamicColor = useDynamicColor,
            buttonDisplayStyle = buttonDisplayStyle
        ) {
            Scaffold(
                bottomBar = {
                    if (isNavigationBarVisible) {
                        AppNavigationBar(
                            currentScreen = currentScreen, onScreenSelected = { screen ->
                                navViewModel.navigateTo(screen)
                            })
                    }
                }) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        when (currentScreen) {
                            is Screen.Home -> HomeScreen()
                            is Screen.Settings -> SettingsScreen(
                                previewThemeMode = themeMode,
                                onThemePreview = { settingsViewModel.setThemeMode(it) },
                                currentThemeType = themeType,
                                onThemeTypeChange = { settingsViewModel.setThemeType(it) },
                                useDynamicColor = useDynamicColor,
                                onDynamicColorChange = { settingsViewModel.setUseDynamicColor(it) },
                                buttonDisplayStyle = buttonDisplayStyle,
                                onButtonDisplayStyleChange = { settingsViewModel.setButtonDisplayStyle(it) },
                                onNavigateToAdvancedLlmSettings = {
                                    navViewModel.navigateTo(Screen.AdvancedLlmSettings)
                                },
                                language = Language.FOLLOW_SYSTEM,
                                onLanguageChange = {})

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
                                TodoPage(viewModel = todoViewModel)
                            }

                            is Screen.CharacterInteraction -> CharacterInteractionPage(
                                onFullScreenChange = { isFullScreen: Boolean ->
                                    navViewModel.setNavigationBarVisible(!isFullScreen)
                                },
                                onNavigateUp = {
                                    navViewModel.navigateTo(Screen.Home)
                                })

                            is Screen.AdvancedLlmSettings -> AdvancedLlmSettingsPage(onNavigateBack = {
                                navViewModel.navigateTo(Screen.Settings)
                            })

                        }
                    }
                }
        }
    }
}
