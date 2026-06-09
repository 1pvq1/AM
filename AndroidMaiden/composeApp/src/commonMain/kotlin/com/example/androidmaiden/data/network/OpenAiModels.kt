package com.example.androidmaiden.data.network

import kotlinx.serialization.Serializable

/**
 * Request model for OpenAI-compatible Chat Completion API.
 */
@Serializable
data class OpenAiChatRequest(
    val model: String,
    val messages: List<OpenAiMessage>,
    val stream: Boolean = true
)

/**
 * Message model for OpenAI-compatible API.
 */
@Serializable
data class OpenAiMessage(
    val role: String,
    val content: String
)

/**
 * Response model for OpenAI-compatible non-streaming Chat Completion API.
 */
@Serializable
data class OpenAiChatResponse(
    val choices: List<OpenAiChoice>
)

/**
 * Choice in the non-streaming response.
 */
@Serializable
data class OpenAiChoice(
    val message: OpenAiMessage
)

/**
 * Response chunk model for OpenAI-compatible streaming API.
 */
@Serializable
data class OpenAiChatResponseChunk(
    val choices: List<OpenAiChoiceChunk>
)

/**
 * Choice chunk in the streaming response.
 */
@Serializable
data class OpenAiChoiceChunk(
    val delta: OpenAiDelta
)

/**
 * Delta content in the streaming response.
 */
@Serializable
data class OpenAiDelta(
    val content: String? = null
)
