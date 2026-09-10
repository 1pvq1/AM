package com.example.androidmaiden.data.local

import androidx.room.*
import com.example.androidmaiden.domain.model.*

/**
 * The "Shadow" Database Entity representing a file or folder on the system.
 * Updated to support rich media metadata and deep-scan status.
 */
@Entity(tableName = "file_metadata")
@OptIn(kotlin.time.ExperimentalTime::class)
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

    fun toDomain(): FileItem = FileItem(
        path = path,
        name = name,
        isDirectory = isDirectory,
        lastModified = lastModified,
        size = size,
        parentPath = parentPath,
        mimeType = mimeType,
        duration = duration,
        artist = artist,
        album = album,
        bitrate = bitrate,
        width = width,
        height = height,
        excerptPath = excerptPath,
        metadataStatus = metadataStatus,
        isFavorite = isFavorite,
        isTrash = isTrash,
        createdAt = kotlin.time.Instant.fromEpochMilliseconds(lastModified)
    )

    companion object {
        fun fromDomain(item: FileItem): FileMetadata = FileMetadata(
            path = item.path,
            name = item.name,
            isDirectory = item.isDirectory,
            lastModified = item.lastModified,
            size = item.size,
            parentPath = item.parentPath,
            mimeType = item.mimeType,
            duration = item.duration,
            artist = item.artist,
            album = item.album,
            bitrate = item.bitrate,
            width = item.width,
            height = item.height,
            excerptPath = item.excerptPath,
            metadataStatus = item.metadataStatus,
            isFavorite = item.isFavorite,
            isTrash = item.isTrash
        )
    }
}

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorHex: String // e.g., "#FF0000"
) {
    fun toDomain(): com.example.androidmaiden.domain.model.Tag = com.example.androidmaiden.domain.model.Tag(
        id = id,
        name = name,
        colorHex = colorHex
    )

    companion object {
        fun fromDomain(item: com.example.androidmaiden.domain.model.Tag): Tag = Tag(
            id = item.id,
            name = item.name,
            colorHex = item.colorHex
        )
    }
}

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

/**
 * Entity representing a chat session.
 */
@Entity
data class ChatSession(
    @PrimaryKey val id: String,
    val title: String,
    val lastMessageAt: Long,
    val providerId: String? = null,
    val isPinned: Boolean = false
) {
    // Session mapping is handled manually in ViewModel or UseCase for now
}

/**
 * Entity representing a single message in a chat session.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId")]
)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val message: String,
    val sender: String, // USER or CHARACTER
    val timestamp: Long
) {
    fun toDomain(): ChatMessage = ChatMessage(
        message = message,
        sender = if (sender == "USER") Sender.USER else Sender.CHARACTER
    )
}

data class FileWithTags(
    @Embedded val file: FileMetadata,
    @Relation(
        parentColumn = "path",
        entityColumn = "id",
        associateBy = Junction(FileTagXRef::class, parentColumn = "path", entityColumn = "tagId")
    )
    val tags: List<Tag>
) {
    fun toDomain(): com.example.androidmaiden.domain.model.FileWithTags = com.example.androidmaiden.domain.model.FileWithTags(
        file = file.toDomain(),
        tags = tags.map { it.toDomain() }
    )
}

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
) {
    fun toDomain(): TrashRecord = TrashRecord(
        originalPath = originalPath,
        trashPath = trashPath,
        deletedAt = deletedAt,
        fileName = fileName,
        size = size,
        isDirectory = isDirectory
    )

    companion object {
        fun fromDomain(item: TrashRecord): TrashEntry = TrashEntry(
            originalPath = item.originalPath,
            trashPath = item.trashPath,
            deletedAt = item.deletedAt,
            fileName = item.fileName,
            size = item.size,
            isDirectory = item.isDirectory
        )
    }
}
