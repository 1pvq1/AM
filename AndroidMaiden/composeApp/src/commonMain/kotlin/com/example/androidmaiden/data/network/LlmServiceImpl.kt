package com.example.androidmaiden.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.*
import io.ktor.http.*
import com.example.androidmaiden.data.repository.SettingsRepository
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json

/**
 * Implementation of LlmService using Ktor for networking.
 * Supports OpenAI-compatible local models (e.g., LM Studio).
 */
class LlmServiceImpl(
    private val client: HttpClient,
    private val settingsRepository: SettingsRepository
) : LlmService {

    private val json = Json { ignoreUnknownKeys = true }

    override fun generateContentStream(prompt: String, history: List<ChatMessage>): Flow<String> = flow {
        // Collect current settings from DataStore
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        
        if (providerId == "local") {
            try {
                // Clean up address: ensure it doesn't end with slash and has /v1 if needed
                var baseUrl = address.trimEnd('/')
                
                // Handle localhost/127.0.0.1 for Android emulator
                if (baseUrl.contains("localhost") || baseUrl.contains("127.0.0.1")) {
                    baseUrl = baseUrl.replace("localhost", "10.0.2.2").replace("127.0.0.1", "10.0.2.2")
                }

                val savedModel = settingsRepository.selectedModel.first()
                val modelId = if (savedModel.isNullOrBlank()) "local-model" else savedModel
                
                val response = client.post("$baseUrl/chat/completions") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        OpenAiChatRequest(
                            model = modelId,
                            messages = history.map { 
                                OpenAiMessage(
                                    role = if (it.sender == ChatSender.USER) "user" else "assistant", 
                                    content = it.message
                                ) 
                            } + OpenAiMessage(role = "user", content = prompt),
                            stream = true
                        )
                    )
                }

                if (response.status == HttpStatusCode.OK) {
                    val channel = response.bodyAsChannel()
                    var hasEmittedAnything = false
                    val fullBodyBuffer = StringBuilder()
                    
                    while (!channel.isClosedForRead) {
                        val line = channel.readUTF8Line() ?: break
                        fullBodyBuffer.append(line).append("\n")
                        
                        if (line.startsWith("data: ")) {
                            val data = line.substring(6).trim()
                            if (data == "[DONE]") break
                            if (data.isEmpty()) continue
                            
                            try {
                                val chunk = json.decodeFromString<OpenAiChatResponseChunk>(data)
                                chunk.choices.firstOrNull()?.delta?.content?.let { 
                                    emit(it) 
                                    hasEmittedAnything = true
                                }
                            } catch (e: Exception) {
                                // Try parsing as full response if chunk parsing fails
                                try {
                                    val fullResponse = json.decodeFromString<OpenAiChatResponse>(data)
                                    fullResponse.choices.firstOrNull()?.message?.content?.let { 
                                        emit(it)
                                        hasEmittedAnything = true
                                    }
                                } catch (innerE: Exception) {
                                    // Ignore
                                    println("Inner parse error: ${innerE.message}")
                                }
                            }
                        }
                    }
                    
                    if (!hasEmittedAnything) {
                        // If nothing was emitted from streaming, try to parse the entire accumulated buffer
                        val fullBody = fullBodyBuffer.toString().trim()
                        if (fullBody.startsWith("{")) {
                            try {
                                val fullResponse = json.decodeFromString<OpenAiChatResponse>(fullBody)
                                fullResponse.choices.firstOrNull()?.message?.content?.let { 
                                    emit(it)
                                    hasEmittedAnything = true
                                }
                            } catch (e: Exception) {
                                try {
                                    val chunk = json.decodeFromString<OpenAiChatResponseChunk>(fullBody)
                                    chunk.choices.firstOrNull()?.delta?.content?.let { 
                                        emit(it)
                                        hasEmittedAnything = true
                                    }
                                } catch (innerE: Exception) {
                                    // Ignore
                                }
                            }
                        }
                    }
                } else {
                    emit("Error from local LLM (${response.status.value}): ${response.status.description}")
                }
            } catch (e: Exception) {
                emit("Error connecting to local LLM: ${e.message}\nCheck if LM Studio is running and the address is correct.")
            }
        } else {
            // Placeholder for Gemini implementation
            emit("Gemini integration is currently under development. Please switch to Local LLM in settings.")
        }
    }.onStart {
        // Optional: emit a loading state or similar if needed
    }

    override suspend fun validate(): Boolean {
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        
        return if (providerId == "local") {
            try {
                var baseUrl = address.trimEnd('/')
                // Handle localhost/127.0.0.1 for Android emulator
                if (baseUrl.contains("localhost") || baseUrl.contains("127.0.0.1")) {
                    baseUrl = baseUrl.replace("localhost", "10.0.2.2").replace("127.0.0.1", "10.0.2.2")
                }
                // Try to get models list to validate connection
                val response = client.get("$baseUrl/models")
                response.status == HttpStatusCode.OK
            } catch (e: Exception) {
                println("Validation error: ${e.message}")
                false
            }
        } else {
            // Simple validation for other providers
            true
        }
    }
}
