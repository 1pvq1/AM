package com.example.androidmaiden.di

import com.example.androidmaiden.data.network.LlmServiceImpl
import com.example.androidmaiden.data.repository.*
import com.example.androidmaiden.domain.themematching.repository.*
import com.example.androidmaiden.data.themematching.repository.*
import com.example.androidmaiden.domain.hardware.repository.HardwareRepository
import com.example.androidmaiden.data.hardware.repository.HardwareRepositoryImpl
import com.example.androidmaiden.presentation.viewmodel.*
import com.example.androidmaiden.domain.service.*
import com.example.androidmaiden.core.experimental.time.*
import com.example.androidmaiden.core.experimental.job.*
import com.example.androidmaiden.data.util.*
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

    // Core Utilities
    single<TimeProvider> { SystemTimeProvider() }

    // 1. Provide a long-lived scope for the Repository
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // 2. Provide the Repository
    single { FileRepository(get(), get(), get(), get()) }
    single { SettingsRepository(get()) }
    single { ChatRepository(get(), get()) }
    single { TodoRepository(get(), get(), get()) }
    single<FileClearRepository> {
        FileClearRepositoryImpl(get(), get(), get(), "/tmp/trash") // Trash dir should be platform-specific in a real app
    }
    single<ThemeMatchingRepository> { ThemeMatchingRepositoryImpl() }
    single<HardwareRepository> { HardwareRepositoryImpl() }

    // 3. Provide the ViewModels
    factory { PersistentFileViewModel(get(), get()) }
    factory { NavigationViewModel() }
    factory { FileScannerViewModel(get(), get()) }
    factory { FileOrganizeViewModel(get()) }
    factory { FileClearViewModel(get()) }
    factory { TodoViewModel(get(), get()) }
    factory { CharacterInteractionViewModel(get(), get(), get(), get()) } // Pass ChatRepository
    factory { AdvancedLlmSettingsViewModel(get(), get(), get(), get()) } // Pass LlmService
    factory { ThemeMatchingViewModel(get()) }
    factory { HardwareViewModel(get()) }
    single { SettingsViewModel(get()) }

    // 4. Provide Services
    single<HostResolver> { DefaultHostResolver() }
    single<LlmService> { LlmServiceImpl(get(), get(), get()) }
}
