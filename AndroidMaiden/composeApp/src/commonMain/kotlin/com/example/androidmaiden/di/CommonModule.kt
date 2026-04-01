package com.example.androidmaiden.di

import com.example.androidmaiden.data.FileRepository
import com.example.androidmaiden.viewModels.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val commonModule = module {
    // 1. Provide a long-lived scope for the Repository
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // 2. Provide the Repository
    single { FileRepository(get(), get(), get()) }

    // 3. Provide the ViewModels
    factory { PersistentFileViewModel(get()) }
    factory { NavigationViewModel() }
    factory { FileScannerViewModel(get()) }
}
