package com.example.androidmaiden.data

import androidx.room.*


/**
* the "Shadow" Database Entity
* */
@Entity(tableName = "file_metadata")
data class FileMetadata(
    @PrimaryKey val path: String, // Absolute path is the unique ID
    val fileName: String,
    val isDirectory: Boolean,
    val lastModified: Long, // Use this to check if the file changed
    val size: Long,
    val parentPath: String // Helpful for "Folder" views
)

