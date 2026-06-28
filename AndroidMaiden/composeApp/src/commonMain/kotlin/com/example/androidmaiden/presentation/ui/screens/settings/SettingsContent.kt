package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

    val contentModifier = if (isWide) {
        Modifier.fillMaxSize().padding(horizontal = 32.dp).widthIn(max = 800.dp)
    } else {
        Modifier.fillMaxSize()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = if (isWide) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Box(modifier = contentModifier) {
            Column {
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
                            language = Language.FOLLOW_SYSTEM, // TODO: Implement language logic
                            onLanguageChange = { }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    item {
                        ChatPersonalizationSettingsGroup()
                    }

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

                    item {
                        NotificationsSettingsGroup()
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    item {
                        AboutSettingsGroup()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContent(
        isWide = false,
        themeMode = ThemeMode.SYSTEM,
        onThemeModeChange = { },
        themeType = AppThemeType.DEFAULT,
        onThemeTypeChange = { },
        useDynamicColor = true,
        onDynamicColorChange = { },
        buttonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
        onButtonDisplayStyleChange = { },
        apiKey = "preview-api-key",
        onApiKeyChange = { },
        localLlmAddress = "http://localhost:1234/v1",
        onLocalLlmAddressChange = { },
        useMatureMarkdown = true,
        onMatureMarkdownToggle = { },
        networkUiState = AdvancedLlmSettingsUiState(),
        onOnlineCheckUrlChange = { },
        checkOnlineConnection = { },
        checkLocalLlmConnection = { },
        onNavigateToAdvancedLlmSettings = { }
    )
}
