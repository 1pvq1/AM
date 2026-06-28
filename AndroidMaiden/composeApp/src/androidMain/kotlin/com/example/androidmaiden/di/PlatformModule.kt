package com.example.androidmaiden.di

import androidx.room.Room
import com.example.androidmaiden.data.local.*
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.util.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific dependency injection module.
 */
val platformModule = module {
    // 0. Provide DataStore
    single { createDataStore(androidContext()) }

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

    // 3. Provide the Android-specific HostResolver
    single<HostResolver> { AndroidHostResolver() }

    // 4. Provide the Android-specific Scanner implementation
    single<FileSystemScanner> { AndroidFileSystemScanner(get()) }

    // 5. Provide the Android-specific FileProvider implementation
    single<FileProvider> { AndroidFileProvider() }
}
