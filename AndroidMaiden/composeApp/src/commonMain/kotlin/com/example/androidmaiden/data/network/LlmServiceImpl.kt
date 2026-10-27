package com.example.androidmaiden.data.network

import io.ktor.client.*
import kotlinx.coroutines.flow.*
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.repository.LocalModelRepository
import com.example.androidmaiden.domain.service.*

/**
 * Core implementation of LlmService.
 * Supports cloud LLM providers (Gemini, OpenAI) as well as on-device local models.
 */
class LlmServiceImpl(
    internal val client: HttpClient,
    private val settingsRepository: SettingsRepository,
    private val hostResolver: HostResolver,
    private val onDeviceInferenceEngine: OnDeviceInferenceEngine? = null,
    private val localModelRepository: LocalModelRepository? = null
) : LlmService {

    override fun generateContentStream(prompt: String, history: List<ChatMessage>): Flow<String> = flow {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        val modelId = settingsRepository.selectedModel.first() ?: "local-model"
        
        val apiKey = when (providerId) {
            "gemini" -> settingsRepository.geminiApiKey.first()
            "openai" -> settingsRepository.openaiApiKey.first()
            "custom" -> settingsRepository.customProviderApiKey.first()
            else -> ""
        }

        when (providerId) {
            "on_device", "local_file" -> {
                val engine = onDeviceInferenceEngine
                val activeModel = localModelRepository?.activeModel?.first()
                if (engine != null && activeModel != null) {
                    if (!engine.isReady()) {
                        engine.initialize(activeModel).getOrThrow()
                    }
                    emitAll(engine.generateResponseStream(prompt, history))
                } else {
                    emit("No on-device local model is initialized or active. Please select a model in LLM Settings.")
                }
            }
            "local" -> {
                val resolvedAddress = hostResolver.resolve(address)
                val localKey = settingsRepository.localApiKey.first()
                emitAll(generateOpenAiStream(prompt, history, resolvedAddress, modelId, localKey))
            }
            else -> {
                emitAll(generateGeminiStream(prompt, history, apiKey))
            }
        }
    }

    override suspend fun validate(): Boolean {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        
        val apiKey = when (providerId) {
            "gemini" -> settingsRepository.geminiApiKey.first()
            "openai" -> settingsRepository.openaiApiKey.first()
            "custom" -> settingsRepository.customProviderApiKey.first()
            else -> ""
        }
        
        return when (providerId) {
            "on_device", "local_file" -> {
                onDeviceInferenceEngine?.isReady() == true || localModelRepository?.activeModel?.first() != null
            }
            "local" -> {
                val resolvedAddress = hostResolver.resolve(address)
                val localKey = settingsRepository.localApiKey.first()
                validateOpenAi(resolvedAddress, localKey)
            }
            else -> {
                validateGemini(apiKey)
            }
        }
    }

    override suspend fun getAvailableModels(): List<String> {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        
        return when (providerId) {
            "on_device", "local_file" -> {
                localModelRepository?.availableModels?.first()?.map { it.name } ?: emptyList()
            }
            "local" -> {
                val resolvedAddress = hostResolver.resolve(address)
                val localKey = settingsRepository.localApiKey.first()
                getOpenAiModels(resolvedAddress, localKey)
            }
            else -> {
                getGeminiModels()
            }
        }
    }
}
