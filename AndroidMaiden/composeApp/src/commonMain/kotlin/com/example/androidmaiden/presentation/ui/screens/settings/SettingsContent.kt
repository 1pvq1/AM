package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.screens.settings.appearance.AppearanceSettingsGroup
import com.example.androidmaiden.presentation.ui.screens.settings.appearance.ChatPersonalizationSettingsGroup
import com.example.androidmaiden.presentation.ui.screens.settings.general.LanguageSettingsGroup
import com.example.androidmaiden.presentation.ui.screens.settings.general.NetworkSettingsContent
import com.example.androidmaiden.presentation.ui.screens.settings.general.NotificationsSettingsGroup
import com.example.androidmaiden.presentation.ui.screens.settings.llm.LlmSettingsContent
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateless UI for the Settings screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    isWide: Boolean,
    selectedSection: SettingsSection,
    onSectionSelect: (SettingsSection) -> Unit,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    themeType: AppThemeType,
    onThemeTypeChange: (AppThemeType) -> Unit,
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    localLlmAddress: String,
    onLocalLlmAddressChange: (String) -> Unit,
    useMatureMarkdown: Boolean,
    onMatureMarkdownToggle: (Boolean) -> Unit,
    networkUiState: AdvancedLlmSettingsUiState,
    onOnlineCheckUrlChange: (String) -> Unit,
    checkOnlineConnection: () -> Unit,
    checkLocalLlmConnection: () -> Unit,
    onNavigateToAdvancedLlmSettings: () -> Unit
) {
    if (isWide) {
        ListDetailSettings(
            selectedSection = selectedSection,
            onSectionSelect = onSectionSelect,
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange,
            themeType = themeType,
            onThemeTypeChange = onThemeTypeChange,
            useDynamicColor = useDynamicColor,
            onDynamicColorChange = onDynamicColorChange,
            buttonDisplayStyle = buttonDisplayStyle,
            onButtonDisplayStyleChange = onButtonDisplayStyleChange,
            apiKey = apiKey,
            onApiKeyChange = onApiKeyChange,
            localLlmAddress = localLlmAddress,
            onLocalLlmAddressChange = onLocalLlmAddressChange,
            useMatureMarkdown = useMatureMarkdown,
            onMatureMarkdownToggle = onMatureMarkdownToggle,
            networkUiState = networkUiState,
            onOnlineCheckUrlChange = onOnlineCheckUrlChange,
            checkOnlineConnection = checkOnlineConnection,
            checkLocalLlmConnection = checkLocalLlmConnection,
            onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings
        )
    } else {
        CompactSettings(
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange,
            themeType = themeType,
            onThemeTypeChange = onThemeTypeChange,
            useDynamicColor = useDynamicColor,
            onDynamicColorChange = onDynamicColorChange,
            buttonDisplayStyle = buttonDisplayStyle,
            onButtonDisplayStyleChange = onButtonDisplayStyleChange,
            apiKey = apiKey,
            onApiKeyChange = onApiKeyChange,
            localLlmAddress = localLlmAddress,
            onLocalLlmAddressChange = onLocalLlmAddressChange,
            useMatureMarkdown = useMatureMarkdown,
            onMatureMarkdownToggle = onMatureMarkdownToggle,
            networkUiState = networkUiState,
            onOnlineCheckUrlChange = onOnlineCheckUrlChange,
            checkOnlineConnection = checkOnlineConnection,
            checkLocalLlmConnection = checkLocalLlmConnection,
            onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListDetailSettings(
    selectedSection: SettingsSection,
    onSectionSelect: (SettingsSection) -> Unit,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    themeType: AppThemeType,
    onThemeTypeChange: (AppThemeType) -> Unit,
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    localLlmAddress: String,
    onLocalLlmAddressChange: (String) -> Unit,
    useMatureMarkdown: Boolean,
    onMatureMarkdownToggle: (Boolean) -> Unit,
    networkUiState: AdvancedLlmSettingsUiState,
    onOnlineCheckUrlChange: (String) -> Unit,
    checkOnlineConnection: () -> Unit,
    checkLocalLlmConnection: () -> Unit,
    onNavigateToAdvancedLlmSettings: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Navigation Pane
        Surface(
            modifier = Modifier.width(280.dp).fillMaxHeight(),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = "settings_title"),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 24.dp, start = 12.dp)
                )
                SettingsSection.entries.forEach { section ->
                    NavigationDrawerItem(
                        icon = { Icon(section.icon, contentDescription = null) },
                        label = { Text(stringResource(id = section.titleResId)) },
                        selected = selectedSection == section,
                        onClick = { onSectionSelect(section) },
                        modifier = Modifier.padding(vertical = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
        }

        // Detail Pane
        Box(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(id = selectedSection.titleResId),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                when (selectedSection) {
                    SettingsSection.APPEARANCE -> {
                        AppearanceSettingsGroup(
                            previewThemeMode = themeMode,
                            onThemePreview = onThemeModeChange,
                            currentThemeType = themeType,
                            onThemeTypeChange = onThemeTypeChange,
                            useDynamicColor = useDynamicColor,
                            onDynamicColorChange = onDynamicColorChange,
                            buttonDisplayStyle = buttonDisplayStyle,
                            onButtonDisplayStyleChange = onButtonDisplayStyleChange
                        )
                    }
                    SettingsSection.LANGUAGE -> {
                        LanguageSettingsGroup(
                            language = Language.FOLLOW_SYSTEM,
                            onLanguageChange = { }
                        )
                    }
                    SettingsSection.CHAT -> {
                        ChatPersonalizationSettingsGroup()
                    }
                    SettingsSection.NETWORK -> {
                        NetworkSettingsContent(
                            uiState = networkUiState,
                            onUrlChange = onOnlineCheckUrlChange,
                            checkConnection = checkOnlineConnection,
                            checkLocalConnection = checkLocalLlmConnection
                        )
                    }
                    SettingsSection.LLM -> {
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
                    SettingsSection.NOTIFICATIONS -> {
                        NotificationsSettingsGroup()
                    }
                    SettingsSection.ABOUT -> {
                        AboutSettingsGroup()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompactSettings(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    themeType: AppThemeType,
    onThemeTypeChange: (AppThemeType) -> Unit,
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    localLlmAddress: String,
    onLocalLlmAddressChange: (String) -> Unit,
    useMatureMarkdown: Boolean,
    onMatureMarkdownToggle: (Boolean) -> Unit,
    networkUiState: AdvancedLlmSettingsUiState,
    onOnlineCheckUrlChange: (String) -> Unit,
    checkOnlineConnection: () -> Unit,
    checkLocalLlmConnection: () -> Unit,
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
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (isSearchActive) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchQuery.isNotEmpty()) searchQuery = "" else isSearchActive = false
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Search results implementation
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                AppearanceSettingsGroup(
                    previewThemeMode = themeMode,
                    onThemePreview = onThemeModeChange,
                    currentThemeType = themeType,
                    onThemeTypeChange = onThemeTypeChange,
                    useDynamicColor = useDynamicColor,
                    onDynamicColorChange = onDynamicColorChange,
                    buttonDisplayStyle = buttonDisplayStyle,
                    onButtonDisplayStyleChange = onButtonDisplayStyleChange
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                LanguageSettingsGroup(
                    language = Language.FOLLOW_SYSTEM,
                    onLanguageChange = { }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { ChatPersonalizationSettingsGroup() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                NetworkSettingsContent(
                    uiState = networkUiState,
                    onUrlChange = onOnlineCheckUrlChange,
                    checkConnection = checkOnlineConnection,
                    checkLocalConnection = checkLocalLlmConnection
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
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
            item { NotificationsSettingsGroup() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { AboutSettingsGroup() }
        }
    }
}
