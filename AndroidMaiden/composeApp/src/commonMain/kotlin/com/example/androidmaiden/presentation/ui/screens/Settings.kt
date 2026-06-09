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
import com.example.androidmaiden.presentation.viewmodel.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

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
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContent(
        previewThemeMode = ThemeMode.SYSTEM,
        onThemePreview = { },
        currentThemeType = AppThemeType.DEFAULT,
        onThemeTypeChange = { },
        useDynamicColor = true,
        onDynamicColorChange = { },
        buttonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
        onButtonDisplayStyleChange = { },
        language = Language.FOLLOW_SYSTEM,
        onLanguageChange = { },
        onNavigateToAdvancedLlmSettings = {},
        networkUiState = AdvancedLlmSettingsUiState(),
        onWebsiteUrlChange = {},
        checkWebsiteConnectivity = {},
        apiKey = "preview-api-key",
        onApiKeyChange = {},
        localLlmAddress = "http://localhost:1234/v1",
        onLocalLlmAddressChange = {},
        useMatureMarkdown=true,
        onMatureMarkdownToggle= {},

    )
}

/**
 * Stateful wrapper for the Settings screen.
 */
@Composable
fun SettingsScreen(
    onNavigateToAdvancedLlmSettings: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    advancedLlmViewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel()
) {
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val themeType by settingsViewModel.themeType.collectAsState()
    val useDynamicColor by settingsViewModel.useDynamicColor.collectAsState()
    val buttonDisplayStyle by settingsViewModel.buttonDisplayStyle.collectAsState()
    val apiKey by settingsViewModel.apiKey.collectAsState()
    val localLlmAddress by settingsViewModel.localLlmAddress.collectAsState()
    val useMatureMarkdown by settingsViewModel.useMatureMarkdown.collectAsState()

    val networkUiState by advancedLlmViewModel.uiState.collectAsState()

    SettingsContent(
        previewThemeMode = themeMode,
        onThemePreview = settingsViewModel::setThemeMode,
        currentThemeType = themeType,
        onThemeTypeChange = settingsViewModel::setThemeType,
        useDynamicColor = useDynamicColor,
        onDynamicColorChange = settingsViewModel::setUseDynamicColor,
        buttonDisplayStyle = buttonDisplayStyle,
        onButtonDisplayStyleChange = settingsViewModel::setButtonDisplayStyle,
        language = Language.FOLLOW_SYSTEM, // TODO: Get from ViewModel
        onLanguageChange = { },
        onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings,
        networkUiState = networkUiState,
        onWebsiteUrlChange = advancedLlmViewModel::onWebsiteUrlChange,
        checkWebsiteConnectivity = advancedLlmViewModel::checkWebsiteConnectivity,
        apiKey = apiKey,
        onApiKeyChange = settingsViewModel::setApiKey,
        localLlmAddress = localLlmAddress,
        onLocalLlmAddressChange = settingsViewModel::setLocalLlmAddress,
        useMatureMarkdown = useMatureMarkdown,
        onMatureMarkdownToggle = settingsViewModel::setUseMatureMarkdown,
    )
}

/**
 * The stateless content of the Settings screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
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
    onNavigateToAdvancedLlmSettings: () -> Unit,
    // Network Settings
    networkUiState: AdvancedLlmSettingsUiState,
    onWebsiteUrlChange: (String) -> Unit,
    checkWebsiteConnectivity: () -> Unit,
    // LLM Settings
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    localLlmAddress: String,
    onLocalLlmAddressChange: (String) -> Unit,
    useMatureMarkdown: Boolean,
    onMatureMarkdownToggle: (Boolean) -> Unit
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
                NetworkSettingsContent(
                    uiState = networkUiState,
                    onWebsiteUrlChange = onWebsiteUrlChange,
                    checkWebsiteConnectivity = checkWebsiteConnectivity
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // LLM Settings Section
            item {
                LlmSettingsContent(
                    apiKey = apiKey,
                    onApiKeyChange = onApiKeyChange,
                    localLlmAddress = localLlmAddress,
                    onLocalLlmAddressChange = onLocalLlmAddressChange,
                    onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings,
                    useMatureMarkdown = useMatureMarkdown,
                    onMatureMarkdownToggle = onMatureMarkdownToggle
                )
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
