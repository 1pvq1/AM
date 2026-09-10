package com.example.androidmaiden.domain.service

import com.example.androidmaiden.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

/**
 * Interface for LLM communication.
 * Module: Chat with LLM
 */
interface LlmService {
    /**
     * Sends a message to the LLM and returns a stream of response chunks.
     */
    fun generateContentStream(prompt: String, history: List<ChatMessage>): Flow<String>

    /**
     * Fetches available models from the current provider.
     */
    suspend fun getAvailableModels(): List<String>
    
    /**
     * Validates if the current configuration (API key, URL, etc.) is working.
     */
    suspend fun validate(): Boolean
}
