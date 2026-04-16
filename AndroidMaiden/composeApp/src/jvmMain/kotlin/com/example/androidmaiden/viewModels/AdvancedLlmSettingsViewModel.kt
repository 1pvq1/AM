package com.example.androidmaiden.viewModels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ktor.client.*
import io.ktor.client.engine.java.*
import com.example.androidmaiden.data.SettingsHolder
import io.ktor.client.statement.*
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
actual fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel {
    return remember { AdvancedLlmSettingsViewModel() }
}

actual class AdvancedLlmSettingsViewModel {

    private val viewModelScope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(
        AdvancedLlmSettingsUiState(
            localLlmAddress = SettingsHolder.localLlmAddress
        )
    )
    actual val uiState = _uiState.asStateFlow()

    private val client = HttpClient(Java)

    actual fun onWebsiteUrlChange(url: String) {
        _uiState.update { it.copy(websiteUrl = url) }
    }

    actual fun checkWebsiteConnectivity() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingWebsite = true, websiteStatus = "") }
            try {
                val response = client.get(_uiState.value.websiteUrl)
                _uiState.update {
                    it.copy(
                        isCheckingWebsite = false,
                        websiteStatus = "Successfully connected. Status: ${response.status}"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCheckingWebsite = false,
                        websiteStatus = "Failed to connect: ${e.message}"
                    )
                }
            }
        }
    }

    actual fun onLocalLlmAddressChange(address: String) {
        _uiState.update { it.copy(localLlmAddress = address) }
        SettingsHolder.localLlmAddress = address
    }

    actual fun connectToLocalLlm() {
        viewModelScope.launch {
            _uiState.update { it.copy(isConnectingToLocalLlm = true, localLlmStatus = "") }
            try {
                val response = client.get(_uiState.value.localLlmAddress)
                _uiState.update {
                    it.copy(
                        isConnectingToLocalLlm = false,
                        localLlmStatus = "Successfully connected. Status: ${response.status}"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isConnectingToLocalLlm = false,
                        localLlmStatus = "Failed to connect: ${e.message}"
                    )
                }
            }
        }
    }
}
