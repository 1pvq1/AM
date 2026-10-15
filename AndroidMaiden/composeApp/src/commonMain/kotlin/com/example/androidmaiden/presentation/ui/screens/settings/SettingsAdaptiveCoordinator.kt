package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    onNavigateToAdvancedLlmSettings: () -> Unit,
    onNavigateToControlAppearance: () -> Unit = {}
) {
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
        onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings,
        onNavigateToControlAppearance = onNavigateToControlAppearance
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun SettingsScreenCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        androidx.compose.material3.Surface {
            SettingsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun SettingsScreenMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        androidx.compose.material3.Surface {
            SettingsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun SettingsScreenExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        androidx.compose.material3.Surface {
            SettingsAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
        }
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
        onNavigateToAdvancedLlmSettings = { },
        onNavigateToControlAppearance = { }
    )
}

