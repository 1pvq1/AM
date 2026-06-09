package com.example.androidmaiden.data.network

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

/**
 * Extension function for handling OpenAI-compatible API calls.
 */
internal fun LlmServiceImpl.generateOpenAiStream(
    prompt: String,
    history: List<ChatMessage>,
    baseUrl: String,
    modelId: String
): Flow<String> = flow {
    try {
        val cleanedBaseUrl = baseUrl.trimEnd('/')
        val response = client.post("$cleanedBaseUrl/chat/completions") {
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
                        try {
                            val fullResponse = json.decodeFromString<OpenAiChatResponse>(data)
                            fullResponse.choices.firstOrNull()?.message?.content?.let {
                                emit(it)
                                hasEmittedAnything = true
                            }
                        } catch (innerE: Exception) {
                            // Ignore
                        }
                    }
                }
            }

            if (!hasEmittedAnything) {
                val fullBody = fullBodyBuffer.toString().trim()
                if (fullBody.startsWith("{")) {
                    try {
                        val fullResponse = json.decodeFromString<OpenAiChatResponse>(fullBody)
                        fullResponse.choices.firstOrNull()?.message?.content?.let {
                            emit(it)
                        }
                    } catch (e: Exception) {
                        try {
                            val chunk = json.decodeFromString<OpenAiChatResponseChunk>(fullBody)
                            chunk.choices.firstOrNull()?.delta?.content?.let {
                                emit(it)
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
}

/**
 * Validates connection to an OpenAI-compatible host.
 */
internal suspend fun LlmServiceImpl.validateOpenAi(baseUrl: String): Boolean {
    return try {
        val cleanedBaseUrl = baseUrl.trimEnd('/')
        val response = client.get("$cleanedBaseUrl/models")
        response.status == HttpStatusCode.OK
    } catch (e: Exception) {
        false
    }
}

/**
 * Fetches available models from an OpenAI-compatible host.
 */
internal suspend fun LlmServiceImpl.getOpenAiModels(baseUrl: String): List<String> {
    return try {
        val cleanedBaseUrl = baseUrl.trimEnd('/')
        val response = client.get("$cleanedBaseUrl/models")
        if (response.status == HttpStatusCode.OK) {
            val modelList = json.decodeFromString<OpenAiModelList>(response.bodyAsText())
            modelList.data.map { it.id }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}
