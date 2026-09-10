package com.example.androidmaiden.di

import com.example.androidmaiden.core.network.NetworkManager
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Dependency injection module for network-related components.
 */
val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }
    
    single { NetworkManager(get()) }
}
