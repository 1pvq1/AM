package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing global application settings and UI control appearance preferences.
 *
 * @param repository The central settings repository backing DataStore preferences.
 */
class SettingsViewModel(private val repository: SettingsRepository) : BaseViewModel() {

    val themeMode: StateFlow<ThemeMode> = repository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val themeType: StateFlow<AppThemeType> = repository.themeType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppThemeType.DEFAULT)

    val useDynamicColor: StateFlow<Boolean> = repository.useDynamicColor
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val buttonDisplayStyle: StateFlow<ButtonDisplayStyle> = repository.buttonDisplayStyle
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ButtonDisplayStyle.ICON_ONLY)

    val apiKey: StateFlow<String> = combine(
        repository.selectedProviderId,
        repository.geminiApiKey,
        repository.openaiApiKey,
        repository.customProviderApiKey
    ) { providerId, gemini, openai, custom ->
        when (providerId) {
            "gemini" -> gemini
            "openai" -> openai
            "custom" -> custom
            else -> ""
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val localLlmAddress: StateFlow<String> = repository.localLlmAddress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val selectedModel: StateFlow<String?> = repository.selectedModel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val useMatureMarkdown: StateFlow<Boolean> = repository.useMatureMarkdown
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /**
     * Observable configuration state for UI controls appearance customization.
     */
    val controlAppearanceConfig: StateFlow<ControlAppearanceConfig> = repository.controlAppearanceConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ControlAppearanceConfig())

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.saveThemeMode(mode) }
    }

    fun setThemeType(type: AppThemeType) {
        viewModelScope.launch { repository.saveThemeType(type) }
    }

    fun setUseDynamicColor(use: Boolean) {
        viewModelScope.launch { repository.saveUseDynamicColor(use) }
    }

    fun setButtonDisplayStyle(style: ButtonDisplayStyle) {
        viewModelScope.launch { repository.saveButtonDisplayStyle(style) }
    }

    fun setApiKey(key: String) {
        viewModelScope.launch {
            val providerId = repository.selectedProviderId.first()
            when (providerId) {
                "gemini" -> repository.saveGeminiApiKey(key)
                "openai" -> repository.saveOpenAiApiKey(key)
                "custom" -> repository.saveCustomProviderApiKey(key)
            }
        }
    }

    fun setLocalLlmAddress(address: String) {
        viewModelScope.launch { repository.saveLocalLlmAddress(address) }
    }

    fun setSelectedModel(model: String) {
        viewModelScope.launch { repository.saveSelectedModel(model) }
    }

    fun setUseMatureMarkdown(use: Boolean) {
        viewModelScope.launch { repository.saveUseMatureMarkdown(use) }
    }

    /**
     * Updates the breadcrumb separator symbol preference.
     */
    fun setBreadcrumbSeparator(separator: BreadcrumbSeparator) {
        viewModelScope.launch { repository.saveBreadcrumbSeparator(separator) }
    }

    /**
     * Updates the breadcrumb maximum visible segments preference.
     */
    fun setBreadcrumbMaxSegments(maxSegments: BreadcrumbMaxSegments) {
        viewModelScope.launch { repository.saveBreadcrumbMaxSegments(maxSegments) }
    }

    /**
     * Updates the breadcrumb root segment display label preference.
     */
    fun setBreadcrumbRootLabel(label: BreadcrumbRootLabel) {
        viewModelScope.launch { repository.saveBreadcrumbRootLabel(label) }
    }

    /**
     * Updates the file item content description display mode preference.
     */
    fun setFileItemDescriptionMode(mode: FileItemDescriptionMode) {
        viewModelScope.launch { repository.saveFileItemDescriptionMode(mode) }
    }

    /**
     * Updates the file item icon style preference.
     */
    fun setFileItemIconStyle(style: FileItemIconStyle) {
        viewModelScope.launch { repository.saveFileItemIconStyle(style) }
    }

    /**
     * Updates whether file item metadata details (timestamp and size/count) are shown.
     */
    fun setFileItemShowDetails(show: Boolean) {
        viewModelScope.launch { repository.saveFileItemShowDetails(show) }
    }

    /**
     * Updates the system hidden files filter mode preference.
     */
    fun setHiddenFilterMode(mode: HiddenFilterMode) {
        viewModelScope.launch { repository.saveHiddenFilterMode(mode) }
    }
}
