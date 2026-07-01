package com.example.androidmaiden.di

import com.example.androidmaiden.data.network.LlmService
import com.example.androidmaiden.data.network.LlmServiceImpl
import com.example.androidmaiden.data.repository.*
import com.example.androidmaiden.domain.themematching.repository.*
import com.example.androidmaiden.data.themematching.repository.*
import com.example.androidmaiden.presentation.viewmodel.*
import com.example.androidmaiden.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

/**
 * Dependency injection module for core application components.
 */
@OptIn(ExperimentalTime::class)
val commonModule = module {
    includes(networkModule)
    // 1. Provide a long-lived scope for the Repository
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // 2. Provide the Repository
    single { FileRepository(get(), get(), get(), get()) }
    single { SettingsRepository(get()) }
    single { ChatRepository(get()) }
    single<FileClearRepository> { 
        FileClearRepositoryImpl(get(), get(), "/tmp/trash") // Trash dir should be platform-specific in a real app
    }
    single<ThemeMatchingRepository> { ThemeMatchingRepositoryImpl() }

    // 3. Provide the ViewModels
    factory { PersistentFileViewModel(get()) }
    factory { NavigationViewModel() }
    factory { FileScannerViewModel(get()) }
    factory { FileOrganizeViewModel(get()) }
    factory { FileClearViewModel(get()) }
    factory { TodoViewModel() }
    factory { CharacterInteractionViewModel(get(), get(), get()) } // Pass ChatRepository
    factory { AdvancedLlmSettingsViewModel(get(), get(), get()) } // Pass LlmService
    factory { ThemeMatchingViewModel(get()) }
    single { SettingsViewModel(get()) }

    // 4. Provide LlmService
    single<HostResolver> { DefaultHostResolver() }
    single<LlmService> { LlmServiceImpl(get(), get(), get()) }
}
