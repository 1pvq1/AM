package com.example.androidmaiden.data.network

/**
 * Expected function for validating an LLM API key.
 */
expect suspend fun validateApiKey(apiKey: String): Boolean
