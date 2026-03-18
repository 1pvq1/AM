package com.example.androidmaiden

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil3.compose.setSingletonImageLoaderFactory
import com.example.androidmaiden.screens.*
import com.example.androidmaiden.screens.pages.AdvancedLlmSettingsPage
import com.example.androidmaiden.screens.pages.CharacterInteractionPage
import com.example.androidmaiden.screens.pages.FileAnalysisScreen
import com.example.androidmaiden.screens.pages.FileClassifyPage
import com.example.androidmaiden.screens.pages.FilesScreen
import com.example.androidmaiden.screens.pages.TodoPage
import com.example.androidmaiden.ui.AppNavigationBar
import com.example.androidmaiden.utils.getAsyncImageLoader
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

// Define dst for navigator
sealed class Screen(val title: String) {
    object Home : Screen("Home")
    object Skills : Screen("Skills")
    object Settings : Screen("Settings")
    object Files : Screen("File Management")
    object FileAnalysis : Screen("File Analysis")
    object FileClassify : Screen("File Classify")
    object Todo : Screen("Todo Lists")
    object CharacterInteraction : Screen("Chat with AI")    
    object AdvancedLlmSettings : Screen("Advanced LLM Settings")
}

@Preview
@Composable
fun App() {
    var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
    var buttonDisplayStyle by remember { mutableStateOf(ButtonDisplayStyle.ICON_ONLY) }

    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var isNavigationBarVisible by remember { mutableStateOf(true) }

    KoinContext {
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
                                    currentScreen = screen
                                    // Reset nav bar visibility when switching screens
                                    isNavigationBarVisible = true
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
                                    currentScreen = Screen.AdvancedLlmSettings
                                },
                                language = Language.FOLLOW_SYSTEM,
                                onLanguageChange = {}
                            )

                            is Screen.Skills -> SkillsPage(onNavigate = { screen ->
                                currentScreen = screen
                            })

                            is Screen.Files -> FilesScreen(onNavigate = { screen ->
                                currentScreen = screen
                            })

                            is Screen.FileAnalysis -> FileAnalysisScreen(onNavigateUp = {
                                currentScreen = Screen.Files
                            })

                            is Screen.FileClassify -> FileClassifyPage(
                                onBack = {
                                    currentScreen = Screen.Files
                                }
                            )

                            is Screen.Todo -> TodoPage()

                            is Screen.CharacterInteraction -> CharacterInteractionPage(
                                onFullScreenChange = { isFullScreen: Boolean ->
                                    isNavigationBarVisible = !isFullScreen
                                },
                                onNavigateUp = {
                                    currentScreen = Screen.Home
                                    isNavigationBarVisible = true
                                }
                            )

                            is Screen.AdvancedLlmSettings -> AdvancedLlmSettingsPage(onNavigateBack = {
                                currentScreen = Screen.Settings
                            })
                        }
                    }
                }
            }
        }
    }
}