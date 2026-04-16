package com.example.androidmaiden.data

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
     * Validates if the current configuration (API key, URL, etc.) is working.
     */
    suspend fun validate(): Boolean
}

data class ChatMessage(
    val message: String,
    val sender: ChatSender,
    val timestamp: Long = 0L // Placeholder for now
)

enum class ChatSender {
    USER,
    CHARACTER
}
