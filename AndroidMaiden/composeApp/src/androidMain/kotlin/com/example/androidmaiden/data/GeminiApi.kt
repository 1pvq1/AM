package com.example.androidmaiden.data

import com.google.ai.client.generativeai.GenerativeModel

actual suspend fun validateApiKey(apiKey: String): Boolean {
    return try {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
        generativeModel.generateContent("test")
        true
    } catch (e: Exception) {
        false
    }
}
