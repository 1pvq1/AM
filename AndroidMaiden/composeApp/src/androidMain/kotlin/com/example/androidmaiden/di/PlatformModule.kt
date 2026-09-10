package com.example.androidmaiden.di

import androidx.room.Room
import com.example.androidmaiden.data.local.*
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.platform.job.AndroidBackgroundJobManager
import com.example.androidmaiden.core.experimental.job.BackgroundJobManager
import com.example.androidmaiden.domain.service.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific dependency injection module.
 */
val platformModule = module {
    // 0. Provide DataStore
    single { createDataStore(androidContext()) }

    // Core Android Services
    single<BackgroundJobManager> { AndroidBackgroundJobManager(androidContext()) }

    // 1. Database Builder (Android-specific)
    single {
        val dbFile = androidContext().getDatabasePath("app_database.db")
        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = dbFile.absolutePath
        )
        .addMigrations(AppDatabase.MIGRATION_2_3)
        // This tells Room to clear the database if the schema version changes and no migration is found.
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
    }

    // 2. Provide the DAO from the Database
    single { get<AppDatabase>().fileMetadataDao() }
    single { get<AppDatabase>().chatDao() }
    single { get<AppDatabase>().todoDao() }

    // 3. Provide the Android-specific HostResolver
    single<HostResolver> { AndroidHostResolver() }

    // 4. Provide the Android-specific Scanner implementation
    single<FileSystemScanner> { AndroidFileSystemScanner(get(), get()) }

    // 5. Provide the Android-specific FileProvider implementation
    single<FileProvider> { AndroidFileProvider() }
}
