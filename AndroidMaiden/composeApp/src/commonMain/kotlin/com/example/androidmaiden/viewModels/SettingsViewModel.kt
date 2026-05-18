package com.example.androidmaiden.viewModels

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.SettingsRepository
import com.example.androidmaiden.ui.theme.core.AppThemeType
import com.example.androidmaiden.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.ui.theme.core.ThemeMode
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
}
