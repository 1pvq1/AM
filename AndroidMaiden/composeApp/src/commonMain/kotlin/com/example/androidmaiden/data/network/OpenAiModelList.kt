package com.example.androidmaiden.data.network

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiModelList(
    val data: List<OpenAiModelItem>
)

@Serializable
data class OpenAiModelItem(
    val id: String
)
