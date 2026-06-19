package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.data.network.validateApiKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun rememberLlmSettingsViewModel(): LlmSettingsViewModel {
    return koinViewModel()
}

actual class LlmSettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LlmSettingsUiState())
    actual val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val savedKey = settingsRepository.geminiApiKey.first()
            _uiState.update { it.copy(apiKey = savedKey) }
        }
    }

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
                    settingsRepository.saveGeminiApiKey(key)
                    val version = 2.5
                    val llmSize = listOf("pro", "flash","flash lite")
                    val models = listOf("gemini$version-$llmSize", "gemini-$version-$llmSize", "gemini-$version-$llmSize")
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
