package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : BaseViewModel() {

    val themeMode: StateFlow<ThemeMode> = repository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val themeType: StateFlow<AppThemeType> = repository.themeType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppThemeType.DEFAULT)

    val useDynamicColor: StateFlow<Boolean> = repository.useDynamicColor
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val buttonDisplayStyle: StateFlow<ButtonDisplayStyle> = repository.buttonDisplayStyle
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ButtonDisplayStyle.ICON_ONLY)

    val apiKey: StateFlow<String> = repository.apiKey
        .map { it ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val localLlmAddress: StateFlow<String> = repository.localLlmAddress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val useMatureMarkdown: StateFlow<Boolean> = repository.useMatureMarkdown
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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
        viewModelScope.launch { repository.saveApiKey(key) }
    }

    fun setLocalLlmAddress(address: String) {
        viewModelScope.launch { repository.saveLocalLlmAddress(address) }
    }

    fun setUseMatureMarkdown(use: Boolean) {
        viewModelScope.launch { repository.saveUseMatureMarkdown(use) }
    }
}
