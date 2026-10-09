package com.example.androidmaiden.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.theme.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for managing application settings using DataStore Preferences.
 * Ensures settings persist across application restarts.
 */
class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val GEMINI_API_KEY = stringPreferencesKey("gemini_api_key")
        val OPENAI_API_KEY = stringPreferencesKey("openai_api_key")
        val CUSTOM_PROVIDER_URL = stringPreferencesKey("custom_provider_url")
        val CUSTOM_PROVIDER_API_KEY = stringPreferencesKey("custom_provider_api_key")
        val LOCAL_API_KEY = stringPreferencesKey("local_api_key")
        
        val SELECTED_MODEL = stringPreferencesKey("selected_model")
        val LOCAL_LLM_ADDRESS = stringPreferencesKey("local_llm_address")
        val SELECTED_PROVIDER_ID = stringPreferencesKey("selected_provider_id")
        
        // Appearance
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val THEME_TYPE = stringPreferencesKey("theme_type")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val BUTTON_DISPLAY_STYLE = stringPreferencesKey("button_display_style")
        val USE_MATURE_MARKDOWN = booleanPreferencesKey("use_mature_markdown")
        val MODEL_CONFIGS_JSON = stringPreferencesKey("model_configs_json")

        // UI Control Appearance Customization
        val BREADCRUMB_SEPARATOR = stringPreferencesKey("breadcrumb_separator")
        val BREADCRUMB_MAX_SEGMENTS = stringPreferencesKey("breadcrumb_max_segments")
        val BREADCRUMB_ROOT_LABEL = stringPreferencesKey("breadcrumb_root_label")
        val FILE_ITEM_DESCRIPTION_MODE = stringPreferencesKey("file_item_description_mode")
        val FILE_ITEM_ICON_STYLE = stringPreferencesKey("file_item_icon_style")
        val FILE_ITEM_SHOW_DETAILS = booleanPreferencesKey("file_item_show_details")
        val HIDDEN_FILTER_MODE = stringPreferencesKey("hidden_filter_mode")
    }

    // LLM Settings
    val geminiApiKey: Flow<String> = dataStore.data.map { it[Keys.GEMINI_API_KEY] ?: "" }
    val openaiApiKey: Flow<String> = dataStore.data.map { it[Keys.OPENAI_API_KEY] ?: "" }
    val customProviderUrl: Flow<String> = dataStore.data.map { it[Keys.CUSTOM_PROVIDER_URL] ?: "" }
    val customProviderApiKey: Flow<String> = dataStore.data.map { it[Keys.CUSTOM_PROVIDER_API_KEY] ?: "" }
    val localApiKey: Flow<String> = dataStore.data.map { it[Keys.LOCAL_API_KEY] ?: "" }

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
    val useMatureMarkdown: Flow<Boolean> = dataStore.data.map { it[Keys.USE_MATURE_MARKDOWN] ?: false }
    val modelConfigsJson: Flow<String?> = dataStore.data.map { it[Keys.MODEL_CONFIGS_JSON] }

    // UI Control Appearance Settings
    val controlAppearanceConfig: Flow<ControlAppearanceConfig> = dataStore.data.map { pref ->
        ControlAppearanceConfig(
            breadcrumbSeparator = BreadcrumbSeparator.entries.find { it.name == pref[Keys.BREADCRUMB_SEPARATOR] } ?: BreadcrumbSeparator.CHEVRON,
            breadcrumbMaxSegments = BreadcrumbMaxSegments.entries.find { it.name == pref[Keys.BREADCRUMB_MAX_SEGMENTS] } ?: BreadcrumbMaxSegments.UNLIMITED,
            breadcrumbRootLabel = BreadcrumbRootLabel.entries.find { it.name == pref[Keys.BREADCRUMB_ROOT_LABEL] } ?: BreadcrumbRootLabel.ROOT,
            fileItemDescriptionMode = FileItemDescriptionMode.entries.find { it.name == pref[Keys.FILE_ITEM_DESCRIPTION_MODE] } ?: FileItemDescriptionMode.CONCISE,
            fileItemIconStyle = FileItemIconStyle.entries.find { it.name == pref[Keys.FILE_ITEM_ICON_STYLE] } ?: FileItemIconStyle.DEFAULT,
            fileItemShowDetails = pref[Keys.FILE_ITEM_SHOW_DETAILS] ?: true,
            hiddenFilterMode = HiddenFilterMode.entries.find { it.name == pref[Keys.HIDDEN_FILTER_MODE] } ?: HiddenFilterMode.SHOW_ALL
        )
    }

    // Save Methods
    suspend fun saveGeminiApiKey(key: String) = dataStore.edit { it[Keys.GEMINI_API_KEY] = key }
    suspend fun saveOpenAiApiKey(key: String) = dataStore.edit { it[Keys.OPENAI_API_KEY] = key }
    suspend fun saveCustomProviderUrl(url: String) = dataStore.edit { it[Keys.CUSTOM_PROVIDER_URL] = url }
    suspend fun saveCustomProviderApiKey(key: String) = dataStore.edit { it[Keys.CUSTOM_PROVIDER_API_KEY] = key }
    suspend fun saveLocalApiKey(key: String) = dataStore.edit { it[Keys.LOCAL_API_KEY] = key }

    suspend fun saveSelectedModel(model: String) = dataStore.edit { it[Keys.SELECTED_MODEL] = model }
    suspend fun saveLocalLlmAddress(address: String) = dataStore.edit { it[Keys.LOCAL_LLM_ADDRESS] = address }
    suspend fun saveSelectedProviderId(id: String) = dataStore.edit { it[Keys.SELECTED_PROVIDER_ID] = id }

    suspend fun saveThemeMode(mode: ThemeMode) = dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    suspend fun saveThemeType(type: AppThemeType) = dataStore.edit { it[Keys.THEME_TYPE] = type.name }
    suspend fun saveUseDynamicColor(use: Boolean) = dataStore.edit { it[Keys.USE_DYNAMIC_COLOR] = use }
    suspend fun saveButtonDisplayStyle(style: ButtonDisplayStyle) = dataStore.edit { it[Keys.BUTTON_DISPLAY_STYLE] = style.name }
    suspend fun saveUseMatureMarkdown(use: Boolean) = dataStore.edit { it[Keys.USE_MATURE_MARKDOWN] = use }
    suspend fun saveModelConfigsJson(json: String) = dataStore.edit { it[Keys.MODEL_CONFIGS_JSON] = json }

    suspend fun saveBreadcrumbSeparator(separator: BreadcrumbSeparator) = dataStore.edit { it[Keys.BREADCRUMB_SEPARATOR] = separator.name }
    suspend fun saveBreadcrumbMaxSegments(maxSegments: BreadcrumbMaxSegments) = dataStore.edit { it[Keys.BREADCRUMB_MAX_SEGMENTS] = maxSegments.name }
    suspend fun saveBreadcrumbRootLabel(label: BreadcrumbRootLabel) = dataStore.edit { it[Keys.BREADCRUMB_ROOT_LABEL] = label.name }
    suspend fun saveFileItemDescriptionMode(mode: FileItemDescriptionMode) = dataStore.edit { it[Keys.FILE_ITEM_DESCRIPTION_MODE] = mode.name }
    suspend fun saveFileItemIconStyle(style: FileItemIconStyle) = dataStore.edit { it[Keys.FILE_ITEM_ICON_STYLE] = style.name }
    suspend fun saveFileItemShowDetails(show: Boolean) = dataStore.edit { it[Keys.FILE_ITEM_SHOW_DETAILS] = show }
    suspend fun saveHiddenFilterMode(mode: HiddenFilterMode) = dataStore.edit { it[Keys.HIDDEN_FILTER_MODE] = mode.name }
}
