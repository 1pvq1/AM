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
}