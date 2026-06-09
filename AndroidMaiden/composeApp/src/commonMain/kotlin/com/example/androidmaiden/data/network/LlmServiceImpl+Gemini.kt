package com.example.androidmaiden.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Extension function for handling Gemini API calls.
 */
internal fun LlmServiceImpl.generateGeminiStream(
    prompt: String,
    history: List<ChatMessage>,
    apiKey: String?
): Flow<String> = flow {
    // Placeholder for real Gemini implementation using generativeai library
    emit("Gemini integration is currently under development. Please switch to Local LLM in settings.")
}

/**
 * Validates Gemini configuration.
 */
internal suspend fun LlmServiceImpl.validateGemini(apiKey: String?): Boolean {
    return !apiKey.isNullOrBlank()
}

/**
 * Returns available Gemini models.
 */
internal suspend fun LlmServiceImpl.getGeminiModels(): List<String> {
    return listOf("gemini-1.5-pro", "gemini-1.5-flash", "gemini-1.5-flash-lite")
}
