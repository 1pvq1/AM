package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.Composable
import com.example.androidmaiden.data.repository.SettingsRepository
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

expect class LlmSettingsViewModel {
    val uiState: StateFlow<LlmSettingsUiState>
    fun onApiKeyChange(apiKey: String)
    fun validateApiKey()
}
