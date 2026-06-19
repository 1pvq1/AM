package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.util.HostResolver
import com.example.androidmaiden.data.network.LlmService
import com.example.androidmaiden.data.network.ModelConfig
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@OptIn(KoinExperimentalAPI::class)
@Composable
actual fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel = koinViewModel()

actual class AdvancedLlmSettingsViewModel actual constructor(
    private val settingsRepository: SettingsRepository,
    private val hostResolver: HostResolver,
    private val llmService: LlmService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdvancedLlmSettingsUiState())
    actual val uiState = _uiState.asStateFlow()

    private val client = HttpClient(OkHttp)
    private val json = Json { ignoreUnknownKeys = true }

    init {
        // Collect all settings from repository
        settingsRepository.localLlmAddress.onEach { v -> _uiState.update { it.copy(localLlmAddress = v) } }.launchIn(viewModelScope)
        settingsRepository.localApiKey.onEach { v -> _uiState.update { it.copy(localApiKey = v) } }.launchIn(viewModelScope)
        settingsRepository.geminiApiKey.onEach { v -> _uiState.update { it.copy(geminiApiKey = v) } }.launchIn(viewModelScope)
        settingsRepository.openaiApiKey.onEach { v -> _uiState.update { it.copy(openaiApiKey = v) } }.launchIn(viewModelScope)
        settingsRepository.customProviderUrl.onEach { v -> _uiState.update { it.copy(customProviderUrl = v) } }.launchIn(viewModelScope)
        settingsRepository.customProviderApiKey.onEach { v -> _uiState.update { it.copy(customProviderApiKey = v) } }.launchIn(viewModelScope)
    }

    actual fun onLocalLlmAddressChange(address: String) {
        viewModelScope.launch {
            settingsRepository.saveLocalLlmAddress(address)
        }
    }

    actual fun onLocalApiKeyChange(key: String) {
        viewModelScope.launch {
            settingsRepository.saveLocalApiKey(key)
        }
    }

    actual fun checkLocalLlmConnection() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingLocalLlm = true, localLlmStatus = "", localHelpText = "") }
            try {
                val resolvedAddress = hostResolver.resolve(_uiState.value.localLlmAddress)
                val response = client.get(resolvedAddress.trimEnd('/') + "/models") {
                    if (_uiState.value.localApiKey.isNotBlank()) {
                        header("Authorization", "Bearer ${_uiState.value.localApiKey}")
                    }
                }
                
                _uiState.update {
                    it.copy(
                        isCheckingLocalLlm = false,
                        localLlmStatus = "Successfully connected. Status: ${response.status}"
                    )
                }
                // Automatically fetch models on successful connection
                fetchAvailableModels()
            } catch (e: Exception) {
                val (status, help) = when {
                    e.message?.contains("CLEARTEXT") == true -> 
                        "Security Policy Blocked" to "Android blocks cleartext (HTTP) by default for security. Ensure your local server is using HTTPS or check our guide for local networking."
                    e.message?.contains("Connection refused") == true ->
                        "Connection Refused" to "Ensure LM Studio is running and 'Server' is active. Also check that your computer's firewall isn't blocking incoming connections on this port."
                    else -> "Connection Failed" to (e.message ?: "Unknown error occurred while connecting.")
                }
                
                _uiState.update {
                    it.copy(
                        isCheckingLocalLlm = false,
                        localLlmStatus = status,
                        localHelpText = help
                    )
                }
            }
        }
    }

    actual fun fetchAvailableModels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingModels = true) }
            val providerId = settingsRepository.selectedProviderId.first()
            val rawModels = llmService.getAvailableModels()
            val currentConfigs = _uiState.value.discoveredModels.toMutableList()

            // Merge new models with existing configurations
            val updatedConfigs = rawModels.mapIndexed { index, modelId ->
                currentConfigs.find { it.id == modelId && it.providerId == providerId } 
                    ?: ModelConfig(id = modelId, providerId = providerId, order = index)
            }

            saveModelConfigs(updatedConfigs)
            _uiState.update { it.copy(isFetchingModels = false) }
        }
    }

    actual fun toggleModelEnabled(modelId: String) {
        viewModelScope.launch {
            val providerId = settingsRepository.selectedProviderId.first()
            val updated = _uiState.value.discoveredModels.map {
                if (it.id == modelId && it.providerId == providerId) it.copy(isEnabled = !it.isEnabled) else it
            }
            saveModelConfigs(updated)
        }
    }

    actual fun updateModelOrder(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            val list = _uiState.value.discoveredModels.toMutableList()
            if (fromIndex in list.indices && toIndex in list.indices) {
                val item = list.removeAt(fromIndex)
                list.add(toIndex, item)
                // Update order values
                val updated = list.mapIndexed { index, config -> config.copy(order = index) }
                saveModelConfigs(updated)
            }
        }
    }

    private suspend fun saveModelConfigs(configs: List<ModelConfig>) {
        val jsonString = json.encodeToString(configs)
        settingsRepository.saveModelConfigsJson(jsonString)
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
            _uiState.update { it.copy(isCheckingOnline = true, onlineCheckStatus = "") }
            try {
                val response = client.get(_uiState.value.onlineCheckUrl)
                _uiState.update {
                    it.copy(
                        isCheckingOnline = false,
                        onlineCheckStatus = "Online Connection OK. Status: ${response.status}"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCheckingOnline = false,
                        onlineCheckStatus = "Online Connection Failed: ${e.message}"
                    )
                }
            }
        }
    }
}
