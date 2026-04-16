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
    val isFavorite: Boolean = false,
    val isTrash: Boolean = false     // New field for trash status
) {
    val extension: String get() = name.substringAfterLast('.', "").lowercase()
    val isFile: Boolean get() = !isDirectory
}

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorHex: String // e.g., "#FF0000"
)

@Entity(
    tableName = "file_tag_xref",
    primaryKeys = ["path", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = FileMetadata::class,
            parentColumns = ["path"],
            childColumns = ["path"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tagId")]
)
data class FileTagXRef(
    val path: String,
    val tagId: Long
)

data class FileWithTags(
    @Embedded val file: FileMetadata,
    @Relation(
        parentColumn = "path",
        entityColumn = "id",
        associateBy = Junction(FileTagXRef::class, parentColumn = "path", entityColumn = "tagId")
    )
    val tags: List<Tag>
)

data class TagWithFiles(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "id",
        entityColumn = "path",
        associateBy = Junction(FileTagXRef::class, parentColumn = "tagId", entityColumn = "path")
    )
    val files: List<FileMetadata>
)

@Entity(tableName = "trash_metadata")
data class TrashEntry(
    @PrimaryKey val originalPath: String,
    val trashPath: String,
    val deletedAt: Long,
    val fileName: String,
    val size: Long,
    val isDirectory: Boolean
)
