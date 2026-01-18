package com.example.androidmaiden

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidmaiden.screens.*
import com.example.androidmaiden.screens.pages.AdvancedLlmSettingsPage
import com.example.androidmaiden.screens.pages.CharacterInteractionPage
import com.example.androidmaiden.screens.pages.FileAnalysisScreen
import com.example.androidmaiden.screens.pages.FilesScreen
import com.example.androidmaiden.screens.pages.TodoPage


// androidMain
@Preview
@Composable
actual fun PlatformApp() {
    var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
    var buttonDisplayStyle by remember { mutableStateOf(ButtonDisplayStyle.ICON_ONLY) }

    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    CompositionLocalProvider(LocalButtonDisplayStyle provides buttonDisplayStyle) {
        MaterialTheme(colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentScreen == Screen.Home,
                            onClick = { currentScreen = Screen.Home },
                            icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
                            label = { Text("首页") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Skills,
                            onClick = { currentScreen = Screen.Skills },
                            icon = { Icon(Icons.Default.Star, contentDescription = "技能区") },
                            label = { Text("技能") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Settings,
                            onClick = { currentScreen = Screen.Settings },
                            icon = { Icon(Icons.Filled.Settings, contentDescription = "设置") },
                            label = { Text("设置") }
                        )
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
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

                        is Screen.FileAnalysis -> FileAnalysisScreen()
                        is Screen.Todo -> TodoPage()
                        is Screen.CharacterInteraction -> CharacterInteractionPage()
                        is Screen.AdvancedLlmSettings -> AdvancedLlmSettingsPage(onNavigateBack = {
                            currentScreen = Screen.Settings
                        })
                    }
                }
            }
        }
    }
}

