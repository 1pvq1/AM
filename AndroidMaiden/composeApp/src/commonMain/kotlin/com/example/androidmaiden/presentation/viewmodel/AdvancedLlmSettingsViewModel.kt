package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.Composable
import com.example.androidmaiden.data.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

data class AdvancedLlmSettingsUiState(
    val localLlmAddress: String = "http://127.0.0.1:1234",
    val localLlmStatus: String = "",
    val isCheckingLocalLlm: Boolean = false,
    val localApiKey: String = "",
    val localHelpLink: String = "https://lmstudio.ai/docs",
    val localHelpText: String = "",
    
    // Online Providers
    val geminiApiKey: String = "",
    val openaiApiKey: String = "",
    val customProviderUrl: String = "",
    val customProviderApiKey: String = "",
    
    // General Network Check
    val onlineCheckUrl: String = "https://www.google.com",
    val onlineCheckStatus: String = "",
    val isCheckingOnline: Boolean = false,

    // Model Management
    val discoveredModels: List<com.example.androidmaiden.data.network.ModelConfig> = emptyList(),
    val isFetchingModels: Boolean = false
)

@Composable
expect fun rememberAdvancedLlmSettingsViewModel(): AdvancedLlmSettingsViewModel

expect class AdvancedLlmSettingsViewModel(
    settingsRepository: SettingsRepository,
    hostResolver: com.example.androidmaiden.util.HostResolver,
    llmService: com.example.androidmaiden.data.network.LlmService
) {
    val uiState: StateFlow<AdvancedLlmSettingsUiState>

    fun onLocalLlmAddressChange(address: String)
    fun onLocalApiKeyChange(key: String)
    fun checkLocalLlmConnection()

    fun onGeminiApiKeyChange(key: String)
    fun onOpenAiApiKeyChange(key: String)
    fun onCustomProviderUrlChange(url: String)
    fun onCustomProviderApiKeyChange(key: String)

    fun onOnlineCheckUrlChange(url: String)
    fun checkOnlineConnection()

    fun fetchAvailableModels()
    fun toggleModelEnabled(modelId: String)
    fun updateModelOrder(fromIndex: Int, toIndex: Int)
}
