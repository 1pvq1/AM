package com.example.androidmaiden.di

import com.example.androidmaiden.data.repository.FileClearRepository
import com.example.androidmaiden.data.repository.FileClearRepositoryImpl
import com.example.androidmaiden.data.repository.FileRepository
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.presentation.viewmodel.*
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
    // 1. Provide a long-lived scope for the Repository
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // 2. Provide the Repository
    single { FileRepository(get(), get(), get()) }
    single { SettingsRepository(get()) }
    single<FileClearRepository> { 
        FileClearRepositoryImpl(get(), get(), "/tmp/trash") // Trash dir should be platform-specific in a real app
    }

    // 3. Provide the ViewModels
    factory { PersistentFileViewModel(get()) }
    factory { NavigationViewModel() }
    factory { FileScannerViewModel(get()) }
    factory { FileOrganizeViewModel(get()) }
    factory { FileClearViewModel(get()) }
    factory { TodoViewModel() }
    factory { CharacterInteractionViewModel(get(), null) } // null for now, or get() if LlmService is in DI
    factory { AdvancedLlmSettingsViewModel(get()) }
    single { SettingsViewModel(get()) }
}
