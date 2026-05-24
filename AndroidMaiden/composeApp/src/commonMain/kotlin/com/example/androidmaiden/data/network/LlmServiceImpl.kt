package com.example.androidmaiden.data.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.*
import io.ktor.http.*
import com.example.androidmaiden.data.repository.SettingsRepository

class LlmServiceImpl(
    private val client: HttpClient,
    private val settingsRepository: SettingsRepository
) : LlmService {

    override fun generateContentStream(prompt: String, history: List<ChatMessage>): Flow<String> = flow {
        // Collect current settings from DataStore
        val providerId = settingsRepository.selectedProviderId.first()
        val address = settingsRepository.localLlmAddress.first()
        
        if (providerId == "local") {
            emit("Connecting to local LLM at $address...\n")
            // Implementation detail: In a real app, use 'client' with 'address' here
            emit("Response from Local LLM: $prompt")
        } else {
            emit("Response from Gemini for: $prompt")
        }
    }

    override suspend fun validate(): Boolean {
        return try {
            true
        } catch (e: Exception) {
            false
        }
    }
}
