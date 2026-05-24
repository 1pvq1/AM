package com.example.androidmaiden.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.androidmaiden.presentation.ui.theme.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for managing application settings using DataStore Preferences.
 * Ensures settings persist across application restarts.
 */
class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val API_KEY = stringPreferencesKey("api_key")
        val SELECTED_MODEL = stringPreferencesKey("selected_model")
        val LOCAL_LLM_ADDRESS = stringPreferencesKey("local_llm_address")
        val SELECTED_PROVIDER_ID = stringPreferencesKey("selected_provider_id")
        
        // Appearance
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val THEME_TYPE = stringPreferencesKey("theme_type")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val BUTTON_DISPLAY_STYLE = stringPreferencesKey("button_display_style")
    }

    // LLM Settings
    val apiKey: Flow<String?> = dataStore.data.map { it[Keys.API_KEY] }
    val selectedModel: Flow<String?> = dataStore.data.map { it[Keys.SELECTED_MODEL] }
    val localLlmAddress: Flow<String> = dataStore.data.map { it[Keys.LOCAL_LLM_ADDRESS] ?: "http://localhost:1234/v1" }
    val selectedProviderId: Flow<String> = dataStore.data.map { it[Keys.SELECTED_PROVIDER_ID] ?: "gemini" }

    // Appearance Settings
    val themeMode: Flow<ThemeMode> = dataStore.data.map { pref ->
        ThemeMode.entries.find { it.name == pref[Keys.THEME_MODE] } ?: ThemeMode.SYSTEM
    }
    val themeType: Flow<AppThemeType> = dataStore.data.map { pref ->
        AppThemeType.entries.find { it.name == pref[Keys.THEME_TYPE] } ?: AppThemeType.DEFAULT
    }
    val useDynamicColor: Flow<Boolean> = dataStore.data.map { it[Keys.USE_DYNAMIC_COLOR] ?: false }
    val buttonDisplayStyle: Flow<ButtonDisplayStyle> = dataStore.data.map { pref ->
        ButtonDisplayStyle.entries.find { it.name == pref[Keys.BUTTON_DISPLAY_STYLE] } ?: ButtonDisplayStyle.ICON_ONLY
    }

    // Save Methods
    suspend fun saveApiKey(key: String) = dataStore.edit { it[Keys.API_KEY] = key }
    suspend fun saveSelectedModel(model: String) = dataStore.edit { it[Keys.SELECTED_MODEL] = model }
    suspend fun saveLocalLlmAddress(address: String) = dataStore.edit { it[Keys.LOCAL_LLM_ADDRESS] = address }
    suspend fun saveSelectedProviderId(id: String) = dataStore.edit { it[Keys.SELECTED_PROVIDER_ID] = id }

    suspend fun saveThemeMode(mode: ThemeMode) = dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    suspend fun saveThemeType(type: AppThemeType) = dataStore.edit { it[Keys.THEME_TYPE] = type.name }
    suspend fun saveUseDynamicColor(use: Boolean) = dataStore.edit { it[Keys.USE_DYNAMIC_COLOR] = use }
    suspend fun saveButtonDisplayStyle(style: ButtonDisplayStyle) = dataStore.edit { it[Keys.BUTTON_DISPLAY_STYLE] = style.name }
}
