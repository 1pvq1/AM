package com.example.androidmaiden.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
* a DAO (Data Access Object) to manage the cache and a Database class to initialize it.
* */
@Dao
interface FileMetadataDao {
    // Instant UI updates via Flow
    @Query("SELECT * FROM file_metadata WHERE parentPath = :path")
    fun getFilesByParent(path: String): Flow<List<FileMetadata>>

    @Query("SELECT * FROM file_metadata")
    fun getAllFiles(): Flow<List<FileMetadata>>

    @Query("SELECT * FROM file_metadata WHERE name LIKE '%' || :query || '%'")
    fun searchFiles(query: String): Flow<List<FileMetadata>>

    @Query("SELECT * FROM file_metadata WHERE isTrash = 0")
    fun getAllNonTrashFiles(): Flow<List<FileMetadata>>

    @Query("SELECT * FROM file_metadata WHERE isTrash = 1")
    fun getTrashFiles(): Flow<List<FileMetadata>>

    @Query("SELECT * FROM file_metadata WHERE size IN (SELECT size FROM file_metadata WHERE isDirectory = 0 AND isTrash = 0 GROUP BY size HAVING COUNT(*) > 1) AND isDirectory = 0 AND isTrash = 0 ORDER BY size DESC")
    fun getDuplicateFiles(): Flow<List<FileMetadata>>

    @Query("SELECT * FROM file_metadata WHERE isDirectory = 1 AND isTrash = 0 AND path NOT IN (SELECT parentPath FROM file_metadata)")
    fun getEmptyFolders(): Flow<List<FileMetadata>>

    // Trash operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrashEntry(entry: TrashEntry)

    @Query("SELECT * FROM trash_metadata ORDER BY deletedAt DESC")
    fun getAllTrashEntries(): Flow<List<TrashEntry>>

    @Query("DELETE FROM trash_metadata WHERE trashPath = :trashPath")
    suspend fun deleteTrashEntry(trashPath: String)

    @Query("SELECT * FROM trash_metadata WHERE trashPath = :trashPath LIMIT 1")
    suspend fun getTrashEntry(trashPath: String): TrashEntry?

    @Query("UPDATE file_metadata SET isTrash = :isTrash WHERE path = :path")
    suspend fun updateTrashStatus(path: String, isTrash: Boolean)

    // Optimization: Bulk insert/update
    @Upsert
    suspend fun upsertFiles(files: List<FileMetadata>)

    @Query("DELETE FROM file_metadata WHERE path = :path")
    suspend fun deleteByPath(path: String)

    @Query("DELETE FROM file_metadata WHERE path IN (:paths)")
    suspend fun deleteByPaths(paths: List<String>)

    @Query("SELECT path FROM file_metadata WHERE parentPath = :path")
    suspend fun getPathsByParent(path: String): List<String>

    @Query("SELECT lastModified FROM file_metadata WHERE path = :path LIMIT 1")
    suspend fun getStoredTimestamp(path: String): Long?

    // Checks if any files in a directory are missing metadata extraction
    @Query("SELECT EXISTS(SELECT 1 FROM file_metadata WHERE parentPath = :path AND metadataStatus = 0 AND isDirectory = 0)")
    suspend fun hasPendingMetadata(path: String): Boolean

    // For Category counts (used in your Strip Blocks)
    @Query("SELECT COUNT(*) FROM file_metadata WHERE isDirectory = 0")
    fun getTotalFileCount(): Flow<Int>

    // --- Tag Management ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileTagXRef(xref: FileTagXRef)

    @Delete
    suspend fun deleteFileTagXRef(xref: FileTagXRef)

    @Transaction
    @Query("SELECT * FROM file_metadata WHERE path = :path")
    fun getFileWithTags(path: String): Flow<FileWithTags?>

    @Transaction
    @Query("SELECT * FROM tags WHERE id = :tagId")
    fun getTagWithFiles(tagId: Long): Flow<TagWithFiles?>

    @Query("SELECT * FROM file_tag_xref WHERE path = :path")
    suspend fun getXRefsForFile(path: String): List<FileTagXRef>
}