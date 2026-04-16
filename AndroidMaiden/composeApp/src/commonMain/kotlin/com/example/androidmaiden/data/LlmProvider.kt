package com.example.androidmaiden.data

import kotlinx.serialization.Serializable

@Serializable
enum class LlmProviderType {
    GEMINI,
    LOCAL_LM_STUDIO,
    CUSTOM_OPENAI_COMPATIBLE
}

@Serializable
data class LlmProvider(
    val id: String,
    val name: String,
    val type: LlmProviderType,
    val baseUrl: String? = null,
    val apiKey: String? = null,
    val modelId: String? = null
)
