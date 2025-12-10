package com.example.androidmaiden.screens.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

data class LlmSettingsUiState(
    val apiKey: String = "",
    val models: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val apiKeyValidated: Boolean = false
)

@Composable
expect fun rememberLlmSettingsViewModel(): LlmSettingsViewModel

expect class LlmSettingsViewModel() : ViewModel {
    val uiState: StateFlow<LlmSettingsUiState>
    fun onApiKeyChange(apiKey: String)
    fun loadModels()
}