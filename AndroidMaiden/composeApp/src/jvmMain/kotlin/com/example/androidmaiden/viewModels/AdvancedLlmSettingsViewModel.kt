package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.Composable
import com.example.androidmaiden.core.network.NetworkManager
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.domain.service.HostResolver
import com.example.androidmaiden.domain.service.LlmService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel = koinViewModel()

actual class AdvancedLlmSettingsViewModel actual constructor(
    private val settingsRepository: SettingsRepository,
    private val hostResolver: HostResolver,
    private val llmService: LlmService,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val _uiState = MutableStateFlow(AdvancedLlmSettingsUiState())
    actual val uiState = _uiState.asStateFlow()

    init {
        // Basic sync for desktop
        settingsRepository.localLlmAddress.onEach { v -> _uiState.update { it.copy(localLlmAddress = v) } }.launchIn(viewModelScope)
    }

    actual fun onLocalLlmAddressChange(address: String) {
        viewModelScope.launch { settingsRepository.saveLocalLlmAddress(address) }
    }

    actual fun onLocalApiKeyChange(key: String) {
        viewModelScope.launch { settingsRepository.saveLocalApiKey(key) }
    }

    actual fun checkLocalLlmConnection() {
        // Logic similar to Android but simplified for desktop for now
    }

    actual fun onGeminiApiKeyChange(key: String) {
        viewModelScope.launch { settingsRepository.saveGeminiApiKey(key) }
    }

    actual fun onOpenAiApiKeyChange(key: String) {
        viewModelScope.launch { settingsRepository.saveOpenAiApiKey(key) }
    }

    actual fun onCustomProviderUrlChange(url: String) {
        viewModelScope.launch { settingsRepository.saveCustomProviderUrl(url) }
    }

    actual fun onCustomProviderApiKeyChange(key: String) {
        viewModelScope.launch { settingsRepository.saveCustomProviderApiKey(key) }
    }

    actual fun onOnlineCheckUrlChange(url: String) {
        _uiState.update { it.copy(onlineCheckUrl = url) }
    }

    actual fun checkOnlineConnection() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOnline = true) }
            try {
                networkManager.checkConnection(_uiState.value.onlineCheckUrl)
                _uiState.update { it.copy(isCheckingOnline = false, onlineCheckStatus = "Connected") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isCheckingOnline = false, onlineCheckStatus = "Failed") }
            }
        }
    }

    actual fun fetchAvailableModels() {}
    actual fun toggleModelEnabled(modelId: String) {}
    actual fun updateModelOrder(fromIndex: Int, toIndex: Int) {}
}
