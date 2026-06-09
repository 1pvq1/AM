package com.example.androidmaiden.data.network

import io.ktor.client.*
import kotlinx.coroutines.flow.*
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.util.HostResolver

/**
 * Core implementation of LlmService.
 * Uses extension functions to delegate logic to specific providers.
 */
class LlmServiceImpl(
    internal val client: HttpClient,
    private val settingsRepository: SettingsRepository,
    private val hostResolver: HostResolver
) : LlmService {

    override fun generateContentStream(prompt: String, history: List<ChatMessage>): Flow<String> = flow {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        val modelId = settingsRepository.selectedModel.first() ?: "local-model"
        val apiKey = settingsRepository.apiKey.first()

        if (providerId == "local") {
            val resolvedAddress = hostResolver.resolve(address)
            emitAll(generateOpenAiStream(prompt, history, resolvedAddress, modelId))
        } else {
            emitAll(generateGeminiStream(prompt, history, apiKey))
        }
    }

    override suspend fun validate(): Boolean {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        val apiKey = settingsRepository.apiKey.first()
        
        return if (providerId == "local") {
            val resolvedAddress = hostResolver.resolve(address)
            validateOpenAi(resolvedAddress)
        } else {
            validateGemini(apiKey)
        }
    }

    override suspend fun getAvailableModels(): List<String> {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        
        return if (providerId == "local") {
            val resolvedAddress = hostResolver.resolve(address)
            getOpenAiModels(resolvedAddress)
        } else {
            getGeminiModels()
        }
    }
}
