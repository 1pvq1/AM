package com.example.androidmaiden.data


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FileMetadata::class], version = 1) // FileMetadata from DBEntity.kt
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileMetadataDao(): FileMetadataDao
}

