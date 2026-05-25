package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import com.example.androidmaiden.data.repository.SettingsRepository
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import io.ktor.client.request.get

@OptIn(KoinExperimentalAPI::class)
@Composable
actual fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel = koinViewModel()

actual class AdvancedLlmSettingsViewModel actual constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdvancedLlmSettingsUiState())
    actual val uiState = _uiState.asStateFlow()

    private val client = HttpClient(OkHttp)

    init {
        settingsRepository.localLlmAddress
            .onEach { address ->
                _uiState.update { it.copy(localLlmAddress = address) }
            }
            .launchIn(viewModelScope)
    }

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
        viewModelScope.launch {
            settingsRepository.saveLocalLlmAddress(address)
        }
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
