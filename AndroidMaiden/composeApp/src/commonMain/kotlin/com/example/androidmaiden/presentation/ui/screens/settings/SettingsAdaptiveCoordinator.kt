package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState

/**
 * The Adaptive Coordinator for the Settings screen.
 */
@Composable
fun SettingsAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
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
    // In Settings, we might use a different layout for wide screens, 
    // e.g., a list-detail view, but for now we'll stick to a single column 
    // with width constraints on wide screens.
    val isWide = windowSizeClass.widthCategory != WindowSizeCategory.Compact
    
    SettingsContent(
        isWide = isWide,
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
