package com.example.androidmaiden.data


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FileMetadata::class], version = 3) // Increased version for schema change
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileMetadataDao(): FileMetadataDao
}

