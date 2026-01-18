package com.example.androidmaiden.screens.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.androidmaiden.viewModels.AdvancedLlmSettingsUiState
import com.example.androidmaiden.viewModels.AdvancedLlmSettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
actual fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel {
    return remember { AdvancedLlmSettingsViewModel() }
}

actual class AdvancedLlmSettingsViewModel : ViewModel() {
    actual val uiState = MutableStateFlow(AdvancedLlmSettingsUiState()).asStateFlow()

    actual fun onWebsiteUrlChange(url: String) {}
    actual fun checkWebsiteConnectivity() {}

    actual fun onLocalLlmAddressChange(address: String) {}
    actual fun connectToLocalLlm() {}
}