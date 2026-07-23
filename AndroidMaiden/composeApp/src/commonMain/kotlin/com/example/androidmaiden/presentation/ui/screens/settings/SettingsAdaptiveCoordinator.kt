package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

/**
 * The Adaptive Coordinator for the Settings screen.
 */
@Composable
fun SettingsAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
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
    // In Settings, we might use a different layout for wide screens, 
    // e.g., a list-detail view,
    // with width constraints on wide screens.
    val isWide = windowSizeClass.widthCategory != WindowSizeCategory.Compact
    
    SettingsContent(
        isWide = isWide,
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
}
@Preview(name = "Compact", showBackground = true)
@Composable
fun SettingsScreenCompactPreview() {
    AppTheme {
        SettingsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
    }
}

@Preview(name = "Medium", showBackground = true)
@Composable
fun SettingsScreenMediumPreview() {
    AppTheme {
        SettingsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
    }
}

@Preview(name = "Expanded", showBackground = true)
@Composable
fun SettingsScreenExpandedPreview() {
    AppTheme {
        SettingsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
    }
}

@Composable
private fun SettingsAdaptiveCoordinatorPreviewHelper(widthCategory: WindowSizeCategory) {
    SettingsAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        selectedSection = SettingsSection.APPEARANCE,
        onSectionSelect = { },
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
