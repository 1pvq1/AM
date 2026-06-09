package com.example.androidmaiden.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

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
    version = 3,
    exportSchema = true,
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

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE ChatSession ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
