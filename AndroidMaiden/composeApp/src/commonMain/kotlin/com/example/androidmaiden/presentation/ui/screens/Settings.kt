package com.example.androidmaiden.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.presentation.ui.theme.core.*
import com.example.androidmaiden.presentation.ui.screens.settings.*
import com.example.androidmaiden.presentation.ui.screens.settings.appearance.*
import com.example.androidmaiden.presentation.ui.screens.settings.general.*
import com.example.androidmaiden.presentation.ui.screens.settings.llm.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Enum representing supported application languages.
 */
enum class Language(val stringResId: String, val tag: String) {
    FOLLOW_SYSTEM("settings_language_system", "system"),
    ENGLISH("settings_language_english", "en"),
    CHINESE("settings_language_chinese", "zh"),
    RUSSIAN("settings_language_russian", "ru"),
    FRENCH("settings_language_french", "fr"),
    ARABIC("settings_language_arabic", "ar");

    /**
     * Returns the localized display name for the language.
     */
    @Composable
    fun getDisplayName(): String {
        return stringResource(id = this.stringResId)
    }
}

/**
 * Preview for the main Settings screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    var previewThemeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
    var previewThemeType by remember { mutableStateOf(AppThemeType.DEFAULT) }
    var buttonDisplayStyle by remember { mutableStateOf(ButtonDisplayStyle.ICON_ONLY) }
    var useDynamicColor by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf(Language.FOLLOW_SYSTEM) }
    SettingsScreen(
        previewThemeMode = previewThemeMode,
        onThemePreview = { previewThemeMode = it },
        currentThemeType = previewThemeType,
        onThemeTypeChange = { previewThemeType = it },
        useDynamicColor = useDynamicColor,
        onDynamicColorChange = { useDynamicColor = it },
        buttonDisplayStyle = buttonDisplayStyle,
        onButtonDisplayStyleChange = { buttonDisplayStyle = it },
        language = language,
        onLanguageChange = { language = it },
        onNavigateToAdvancedLlmSettings = {}
    )
}

/**
 * The main Settings screen of the application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit,
    currentThemeType: AppThemeType,
    onThemeTypeChange: (AppThemeType) -> Unit,
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    onNavigateToAdvancedLlmSettings: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { isSearchActive = false },
            active = isSearchActive,
            onActiveChange = { isSearchActive = it },
            placeholder = { Text(stringResource(id = "settings_search_placeholder")) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(id = "settings_search_icon_description")) },
            trailingIcon = {
                if (isSearchActive) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchQuery.isNotEmpty()) searchQuery = "" else isSearchActive =
                                false
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = "settings_close_search_icon_description")
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        ) {
            // TODO: Implement search results
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // Appearance Section
            item {
                AppearanceSettingsGroup(
                    previewThemeMode = previewThemeMode,
                    onThemePreview = onThemePreview,
                    currentThemeType = currentThemeType,
                    onThemeTypeChange = onThemeTypeChange,
                    useDynamicColor = useDynamicColor,
                    onDynamicColorChange = onDynamicColorChange,
                    buttonDisplayStyle = buttonDisplayStyle,
                    onButtonDisplayStyleChange = onButtonDisplayStyleChange
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Language Section
            item {
                LanguageSettingsGroup(
                    language = language,
                    onLanguageChange = onLanguageChange
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Chat Personalization Section
            item {
                ChatPersonalizationSettingsGroup()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Network Section
            item {
                NetworkSettingsGroup()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // LLM Settings Section
            item {
                LlmSettingsGroup(onNavigateToAdvancedLlmSettings)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Notifications Section
            item {
                NotificationsSettingsGroup()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // About Section
            item {
                AboutSettingsGroup()
            }
        }
    }
}
