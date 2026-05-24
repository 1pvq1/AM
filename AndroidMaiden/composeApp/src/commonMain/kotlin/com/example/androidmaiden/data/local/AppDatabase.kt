package com.example.androidmaiden.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room database for the application.
 */
@Database(
    entities = [FileMetadata::class, Tag::class, FileTagXRef::class, TrashEntry::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to the file metadata DAO.
     */
    abstract fun fileMetadataDao(): FileMetadataDao
}
