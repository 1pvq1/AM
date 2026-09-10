package com.example.androidmaiden.core.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

/**
 * Centralized manager for network operations and Ktor configuration.
 */
class NetworkManager(private val client: HttpClient) {

    /**
     * Checks connectivity to a specific URL.
     */
    suspend fun checkConnection(url: String): HttpResponse {
        return client.get(url)
    }

    /**
     * Provides access to the shared HttpClient.
     * Use this sparingly; prefer adding specific methods to NetworkManager.
     */
    fun getClient(): HttpClient = client
}
