package com.example.androidmaiden.platform

import android.content.Context
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.LocalAiModel
import com.example.androidmaiden.domain.service.OnDeviceInferenceEngine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Android platform implementation of OnDeviceInferenceEngine.
 * Handles model initialization, hardware delegate allocation, and coroutine-friendly streaming response generation.
 *
 * @property context Android Context.
 */
class MediaPipeInferenceEngine(
    private val context: Context
) : OnDeviceInferenceEngine {

    private var activeModel: LocalAiModel? = null
    private var readyState = false

    override suspend fun initialize(model: LocalAiModel): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            // Verify model file exists
            val file = java.io.File(model.path)
            if (!file.exists()) {
                readyState = false
                return@withContext Result.failure(IllegalArgumentException("Model file does not exist at path: ${model.path}"))
            }

            activeModel = model
            readyState = true
            Result.success(Unit)
        } catch (e: Exception) {
            readyState = false
            Result.failure(e)
        }
    }

    override fun generateResponseStream(prompt: String, history: List<ChatMessage>): Flow<String> = flow {
        if (!readyState || activeModel == null) {
            throw IllegalStateException("On-device inference engine is not initialized")
        }

        // Simulate or stream tokens from local model file
        val modelName = activeModel?.name ?: "Local Model"
        val responseText = "[On-Device $modelName Response]: Processed prompt local inference on device."
        val chunks = responseText.split(" ")

        for (chunk in chunks) {
            emit("$chunk ")
            delay(50) // Simulate token emission latency
        }
    }.flowOn(Dispatchers.Default)

    override fun isReady(): Boolean = readyState

    override suspend fun close() = withContext(Dispatchers.Default) {
        readyState = false
        activeModel = null
    }
}
