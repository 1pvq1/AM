package com.example.androidmaiden.viewModels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.androidmaiden.data.SettingsHolder
import com.example.androidmaiden.data.validateApiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
actual fun rememberLlmSettingsViewModel(): LlmSettingsViewModel {
    return remember { LlmSettingsViewModel() }
}

actual class LlmSettingsViewModel {

    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow(
        LlmSettingsUiState(
            apiKey = SettingsHolder.apiKey ?: ""
        )
    )
    actual val uiState = _uiState.asStateFlow()

    actual fun onApiKeyChange(apiKey: String) {
        _uiState.update { it.copy(apiKey = apiKey, apiKeyValidated = false) }
    }

    actual fun validateApiKey() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, apiKeyValidated = false) }
            try {
                val key = _uiState.value.apiKey
                if (key.isBlank()) {
                    throw Exception("API key cannot be empty")
                }
                val isValid = validateApiKey(key)
                if (isValid) {
                    SettingsHolder.apiKey = key
                    val models = listOf("gemini-1.5-pro-latest", "gemini-1.5-flash-latest", "gemini-1.0-pro")
                    _uiState.update {
                        it.copy(
                            models = models,
                            isLoading = false,
                            apiKeyValidated = true
                        )
                    }
                } else {
                    throw Exception("Invalid API key")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "An unknown error occurred",
                        isLoading = false,
                        models = emptyList()
                    )
                }
            }
        }
    }
}
