package com.example.androidmaiden.domain.service

import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.LocalAiModel
import kotlinx.coroutines.flow.*

/**
 * Interface representing an on-device LLM inference engine.
 * Abstracts low-level runtime engines (e.g., MediaPipe LLM Inference, TFLite) in platform-neutral domain code.
 */
interface OnDeviceInferenceEngine {

    /**
     * Initializes the engine with the specified local model.
     *
     * @param model Local model entity containing file path and configuration.
     * @return Result indicating successful initialization or execution failure.
     */
    suspend fun initialize(model: LocalAiModel): Result<Unit>

    /**
     * Sends a prompt and message history to the local model and streams response tokens chunk by chunk.
     *
     * @param prompt User prompt text.
     * @param history Chat message history context.
     * @return Flow emitting generated response string chunks.
     */
    fun generateResponseStream(prompt: String, history: List<ChatMessage> = emptyList()): Flow<String>

    /**
     * Checks if the inference engine is currently initialized and ready to handle prompts.
     *
     * @return True if initialized, false otherwise.
     */
    fun isReady(): Boolean

    /**
     * Releases hardware resources and closes the active model session.
     */
    suspend fun close()
}
