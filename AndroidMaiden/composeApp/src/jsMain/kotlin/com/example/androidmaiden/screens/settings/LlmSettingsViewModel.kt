package com.example.androidmaiden.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
actual fun rememberLlmSettingsViewModel(): LlmSettingsViewModel {
    return remember { LlmSettingsViewModel() }
}

actual class LlmSettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LlmSettingsUiState(models = listOf("JS-Model-1", "JS-Model-2")))
    actual val uiState = _uiState.asStateFlow()

    actual fun onApiKeyChange(apiKey: String) {}

    actual fun loadModels() {}
}