package com.example.androidmaiden.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing file metadata and related entities in the local database.
 */
@Dao
interface FileMetadataDao {
    /**
     * Returns a flow of file metadata for children of the given parent path.
     */
    @Query("SELECT * FROM file_metadata WHERE parentPath = :path")
    fun getFilesByParent(path: String): Flow<List<FileMetadata>>

    /**
     * Returns a flow of all file metadata records.
     */
    @Query("SELECT * FROM file_metadata")
    fun getAllFiles(): Flow<List<FileMetadata>>

    /**
     * Searches for files by name.
     */
    @Query("SELECT * FROM file_metadata WHERE name LIKE '%' || :query || '%'")
    fun searchFiles(query: String): Flow<List<FileMetadata>>

    /**
     * Returns all files that are not in the trash.
     */
    @Query("SELECT * FROM file_metadata WHERE isTrash = 0")
    fun getAllNonTrashFiles(): Flow<List<FileMetadata>>

    /**
     * Returns all files currently in the trash.
     */
    @Query("SELECT * FROM file_metadata WHERE isTrash = 1")
    fun getTrashFiles(): Flow<List<FileMetadata>>

    /**
     * Returns duplicate files based on size.
     */
    @Query("SELECT * FROM file_metadata WHERE size IN (SELECT size FROM file_metadata WHERE isDirectory = 0 AND isTrash = 0 GROUP BY size HAVING COUNT(*) > 1) AND isDirectory = 0 AND isTrash = 0 ORDER BY size DESC")
    fun getDuplicateFiles(): Flow<List<FileMetadata>>

    /**
     * Returns empty folders that are not in the trash.
     */
    @Query("SELECT * FROM file_metadata WHERE isDirectory = 1 AND isTrash = 0 AND path NOT IN (SELECT parentPath FROM file_metadata)")
    fun getEmptyFolders(): Flow<List<FileMetadata>>

    /**
     * Inserts a new trash entry.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrashEntry(entry: TrashEntry)

    /**
     * Returns all trash metadata entries ordered by deletion date.
     */
    @Query("SELECT * FROM trash_metadata ORDER BY deletedAt DESC")
    fun getAllTrashEntries(): Flow<List<TrashEntry>>

    /**
     * Deletes a trash entry by its trash path.
     */
    @Query("DELETE FROM trash_metadata WHERE trashPath = :trashPath")
    suspend fun deleteTrashEntry(trashPath: String)

    /**
     * Retrieves a single trash entry by its trash path.
     */
    @Query("SELECT * FROM trash_metadata WHERE trashPath = :trashPath LIMIT 1")
    suspend fun getTrashEntry(trashPath: String): TrashEntry?

    /**
     * Updates the trash status of a file.
     */
    @Query("UPDATE file_metadata SET isTrash = :isTrash WHERE path = :path")
    suspend fun updateTrashStatus(path: String, isTrash: Boolean)

    /**
     * Bulk inserts or updates file metadata records.
     */
    @Upsert
    suspend fun upsertFiles(files: List<FileMetadata>)

    /**
     * Deletes a file metadata record by its path.
     */
    @Query("DELETE FROM file_metadata WHERE path = :path")
    suspend fun deleteByPath(path: String)

    /**
     * Deletes multiple file metadata records by their paths.
     */
    @Query("DELETE FROM file_metadata WHERE path IN (:paths)")
    suspend fun deleteByPaths(paths: List<String>)

    /**
     * Returns a list of paths for children of the given parent path.
     */
    @Query("SELECT path FROM file_metadata WHERE parentPath = :path")
    suspend fun getPathsByParent(path: String): List<String>

    /**
     * Returns the stored last modified timestamp for a path.
     */
    @Query("SELECT lastModified FROM file_metadata WHERE path = :path LIMIT 1")
    suspend fun getStoredTimestamp(path: String): Long?

    /**
     * Checks if any files in a directory are missing metadata extraction.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM file_metadata WHERE parentPath = :path AND metadataStatus = 0 AND isDirectory = 0)")
    suspend fun hasPendingMetadata(path: String): Boolean

    /**
     * Returns the total count of files (excluding directories).
     */
    @Query("SELECT COUNT(*) FROM file_metadata WHERE isDirectory = 0")
    fun getTotalFileCount(): Flow<Int>

    // --- Tag Management ---

    /**
     * Inserts a new tag and returns its ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    /**
     * Deletes an existing tag.
     */
    @Delete
    suspend fun deleteTag(tag: Tag)

    /**
     * Returns a flow of all available tags.
     */
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<Tag>>

    /**
     * Inserts a file-tag cross-reference record.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileTagXRef(xref: FileTagXRef)

    /**
     * Deletes a file-tag cross-reference record.
     */
    @Delete
    suspend fun deleteFileTagXRef(xref: FileTagXRef)

    /**
     * Returns a flow of a file and its associated tags.
     */
    @Transaction
    @Query("SELECT * FROM file_metadata WHERE path = :path")
    fun getFileWithTags(path: String): Flow<FileWithTags?>

    /**
     * Returns a flow of a tag and its associated files.
     */
    @Transaction
    @Query("SELECT * FROM tags WHERE id = :tagId")
    fun getTagWithFiles(tagId: Long): Flow<TagWithFiles?>

    /**
     * Returns all cross-reference records for a specific file path.
     */
    @Query("SELECT * FROM file_tag_xref WHERE path = :path")
    suspend fun getXRefsForFile(path: String): List<FileTagXRef>
}
