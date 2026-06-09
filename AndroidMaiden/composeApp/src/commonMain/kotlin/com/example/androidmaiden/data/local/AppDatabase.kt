package com.example.androidmaiden.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room database for the application.
 */
@Database(
    entities = [
        FileMetadata::class, 
        Tag::class, 
        FileTagXRef::class, 
        TrashEntry::class,
        ChatSession::class,
        ChatMessageEntity::class,
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to the file metadata DAO.
     */
    abstract fun fileMetadataDao(): FileMetadataDao

    /**
     * Provides access to the chat DAO.
     */
    abstract fun chatDao(): ChatDao
}
