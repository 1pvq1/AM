package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.runtime.*
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsViewModel
import com.example.androidmaiden.presentation.viewmodel.SettingsViewModel
import com.example.androidmaiden.presentation.viewmodel.rememberAdvancedLlmSettingsViewModel
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
 * The Stateful Entry Point for the Settings screen.
 */
@Composable
fun SettingsScreen(
    onNavigateToAdvancedLlmSettings: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    advancedLlmViewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel(),
) {
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val themeType by settingsViewModel.themeType.collectAsState()
    val useDynamicColor by settingsViewModel.useDynamicColor.collectAsState()
    val buttonDisplayStyle by settingsViewModel.buttonDisplayStyle.collectAsState()
    val apiKey by settingsViewModel.apiKey.collectAsState()
    val localLlmAddress by settingsViewModel.localLlmAddress.collectAsState()
    val useMatureMarkdown by settingsViewModel.useMatureMarkdown.collectAsState()

    val networkUiState by advancedLlmViewModel.uiState.collectAsState()
    
    val windowSizeClass = LocalWindowSizeClass.current

    SettingsAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        themeMode = themeMode,
        onThemeModeChange = settingsViewModel::setThemeMode,
        themeType = themeType,
        onThemeTypeChange = settingsViewModel::setThemeType,
        useDynamicColor = useDynamicColor,
        onDynamicColorChange = settingsViewModel::setUseDynamicColor,
        buttonDisplayStyle = buttonDisplayStyle,
        onButtonDisplayStyleChange = settingsViewModel::setButtonDisplayStyle,
        apiKey = apiKey,
        onApiKeyChange = settingsViewModel::setApiKey,
        localLlmAddress = localLlmAddress,
        onLocalLlmAddressChange = settingsViewModel::setLocalLlmAddress,
        useMatureMarkdown = useMatureMarkdown,
        onMatureMarkdownToggle = settingsViewModel::setUseMatureMarkdown,
        networkUiState = networkUiState,
        onOnlineCheckUrlChange = advancedLlmViewModel::onOnlineCheckUrlChange,
        checkOnlineConnection = advancedLlmViewModel::checkOnlineConnection,
        checkLocalLlmConnection = advancedLlmViewModel::checkLocalLlmConnection,
        onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings
    )
}
