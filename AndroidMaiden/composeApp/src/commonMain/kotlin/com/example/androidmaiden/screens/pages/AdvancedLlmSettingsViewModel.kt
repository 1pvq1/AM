package com.example.androidmaiden.screens.pages

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

data class AdvancedLlmSettingsUiState(
    val websiteUrl: String = "https://www.google.com",
    val websiteStatus: String = "",
    val localLlmAddress: String = "http://127.0.0.1:1234",
    val localLlmStatus: String = "",
    val isCheckingWebsite: Boolean = false,
    val isConnectingToLocalLlm: Boolean = false
)

@Composable
expect fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel

expect class AdvancedLlmSettingsViewModel() : ViewModel {
    val uiState: StateFlow<AdvancedLlmSettingsUiState>

    fun onWebsiteUrlChange(url: String)
    fun checkWebsiteConnectivity()

    fun onLocalLlmAddressChange(address: String)
    fun connectToLocalLlm()
}