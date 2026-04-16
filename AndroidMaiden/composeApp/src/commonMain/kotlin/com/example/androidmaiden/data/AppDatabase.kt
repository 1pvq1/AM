package com.example.androidmaiden.data


import androidx.room.Database
import androidx.room.RoomDatabase

/** Increased version for schema change
 */
@Database(
    entities = [FileMetadata::class, Tag::class, FileTagXRef::class, TrashEntry::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileMetadataDao(): FileMetadataDao
}

