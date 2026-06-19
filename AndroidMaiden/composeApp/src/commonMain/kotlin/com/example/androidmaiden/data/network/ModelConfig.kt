package com.example.androidmaiden.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ModelConfig(
    val id: String,
    val providerId: String,
    val isEnabled: Boolean = true,
    val order: Int = 0
)
