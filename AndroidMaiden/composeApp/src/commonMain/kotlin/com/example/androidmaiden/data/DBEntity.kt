package com.example.androidmaiden.data

import androidx.room.*

/**
 * The "Shadow" Database Entity representing a file or folder on the system.
 * Updated to support rich media metadata and deep-scan status.
 */
@Entity(tableName = "file_metadata")
data class FileMetadata(
    @PrimaryKey val path: String,     // Absolute path is the unique ID
    val name: String,            // Physical file name
    val isDirectory: Boolean,
    val lastModified: Long,          // Epoch millis; Use this to check if the file changed
    val size: Long,                  // Bytes
    val parentPath: String,          // Path of the containing folder; Helpful for "Folder" views

    // --- Media Metadata (Optional, populated during deep scan) ---
    val mimeType: String? = null,
    val duration: Long? = null,      // Milliseconds
    val artist: String? = null,
    val album: String? = null,
    val bitrate: Long? = null,       // bps
    val width: Int? = null,          // Resolution Width
    val height: Int? = null,         // Resolution Height
    
    // --- Thumbnail / Preview ---
    val excerptPath: String? = null, // Path to generated preview/excerpt file
    
    // --- State Management ---
    val metadataStatus: Int = 0,     // 0: Pending, 1: Extracted, 2: Failed
    val isFavorite: Boolean = false
) {
    val extension: String get() = name.substringAfterLast('.', "").lowercase()
    val isFile: Boolean get() = !isDirectory
}
