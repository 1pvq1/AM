package com.example.androidmaiden.di

import androidx.room.Room
import com.example.androidmaiden.data.AppDatabase
import com.example.androidmaiden.utils.AndroidFileSystemScanner
import com.example.androidmaiden.utils.FileSystemScanner
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    // 1. Database Builder (Android-specific)
    single {
        val dbFile = androidContext().getDatabasePath("app_database.db")
        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = dbFile.absolutePath
        )
        // This tells Room to clear the database if the schema version changes.
        // Useful for development to avoid manual migrations.
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
    }

    // 2. Provide the DAO from the Database
    single { get<AppDatabase>().fileMetadataDao() }

    // 3. Provide the Android-specific Scanner implementation
    single<FileSystemScanner> { AndroidFileSystemScanner(get()) }
}