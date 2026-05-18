package com.example.androidmaiden.viewModels

import androidx.compose.runtime.Composable
import com.example.androidmaiden.data.SettingsRepository
import com.example.androidmaiden.data.validateApiKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
actual fun rememberLlmSettingsViewModel(): LlmSettingsViewModel {
    val repository: SettingsRepository = koinInject()
    return LlmSettingsViewModel(repository)
}

actual class LlmSettingsViewModel(
    private val settingsRepository: SettingsRepository
) {
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _uiState = MutableStateFlow(LlmSettingsUiState())
    actual val uiState = _uiState.asStateFlow()

    init {
        settingsRepository.apiKey
            .onEach { key ->
                _uiState.update { it.copy(apiKey = key ?: "") }
            }
            .launchIn(viewModelScope)
    }

    actual fun onApiKeyChange(apiKey: String) {
        _uiState.update { it.copy(apiKey = apiKey, apiKeyValidated = false) }
    }

    actual fun validateApiKey() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, apiKeyValidated = false) }
            try {
                val key = _uiState.value.apiKey
                if (key.isBlank()) throw Exception("API key cannot be empty")
                
                val isValid = validateApiKey(key)
                if (isValid) {
                    settingsRepository.saveApiKey(key)
                    val models = listOf("gemini-1.5-pro-latest", "gemini-1.5-flash-latest", "gemini-1.0-pro")
                    _uiState.update {
                        it.copy(models = models, isLoading = false, apiKeyValidated = true)
                    }
                } else {
                    throw Exception("Invalid API key")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "An unknown error occurred", isLoading = false, models = emptyList())
                }
            }
        }
    }
}
