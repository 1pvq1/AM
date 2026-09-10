package com.example.androidmaiden.data.repository

import com.example.androidmaiden.core.experimental.time.TimeProvider
import com.example.androidmaiden.domain.model.CleanupStats
import com.example.androidmaiden.domain.model.TrashRecord
import com.example.androidmaiden.domain.service.FileSystemScanner
import com.example.androidmaiden.data.local.*
import kotlinx.coroutines.flow.*

interface FileClearRepository {
    val cleanupStats: Flow<CleanupStats>
    val trashEntries: Flow<List<TrashRecord>>
    
    suspend fun moveToTrash(path: String): Boolean
    suspend fun restoreFromTrash(trashPath: String): Boolean
    suspend fun deletePermanently(trashPath: String): Boolean
    suspend fun emptyTrash(): Boolean
    suspend fun deleteFiles(paths: List<String>): Boolean
    suspend fun clearEmptyFolders(): Int
}

class FileClearRepositoryImpl(
    private val fileDao: FileMetadataDao,
    private val scanner: FileSystemScanner,
    private val timeProvider: TimeProvider,
    private val trashDir: String // Should be provided via platform module
) : FileClearRepository {

    override val cleanupStats: Flow<CleanupStats> = combine(
        fileDao.getDuplicateFiles(),
        fileDao.getEmptyFolders(),
        fileDao.getAllTrashEntries()
    ) { duplicates, emptyFolders, trash ->
        CleanupStats(
            duplicateFiles = duplicates.map { it.toDomain() },
            emptyFolders = emptyFolders.map { it.toDomain() },
            trashEntries = trash.map { it.toDomain() }
        )
    }

    override val trashEntries: Flow<List<TrashRecord>> = fileDao.getAllTrashEntries().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun moveToTrash(path: String): Boolean {
        val fileName = path.substringAfterLast('/')
        val now = timeProvider.nowMillis()
        val destination = "$trashDir/${now}_$fileName"
        
        val success = scanner.moveFile(path, destination)
        if (success) {
            val entry = TrashEntry(
                originalPath = path,
                trashPath = destination,
                deletedAt = now,
                fileName = fileName,
                size = 0, // Should be fetched from DB if possible
                isDirectory = false
            )
            fileDao.insertTrashEntry(entry)
            fileDao.updateTrashStatus(path, true)
        }
        return success
    }

    override suspend fun restoreFromTrash(trashPath: String): Boolean {
        val entry = fileDao.getTrashEntry(trashPath) ?: return false
        val success = scanner.moveFile(trashPath, entry.originalPath)
        if (success) {
            fileDao.deleteTrashEntry(trashPath)
            fileDao.updateTrashStatus(entry.originalPath, false)
        }
        return success
    }

    override suspend fun deletePermanently(trashPath: String): Boolean {
        val success = scanner.deleteFile(trashPath)
        if (success) {
            fileDao.deleteTrashEntry(trashPath)
        }
        return success
    }

    override suspend fun emptyTrash(): Boolean {
        val trashFiles = fileDao.getAllTrashEntries().first()
        var allSuccess = true
        trashFiles.forEach { entry ->
            if (scanner.deleteFile(entry.trashPath)) {
                fileDao.deleteTrashEntry(entry.trashPath)
            } else {
                allSuccess = false
            }
        }
        return allSuccess
    }

    override suspend fun deleteFiles(paths: List<String>): Boolean {
        var allSuccess = true
        paths.forEach { path ->
            if (scanner.deleteFile(path)) {
                fileDao.deleteByPath(path)
            } else {
                allSuccess = false
            }
        }
        return allSuccess
    }

    override suspend fun clearEmptyFolders(): Int {
        val emptyFolders = fileDao.getEmptyFolders().first()
        var count = 0
        emptyFolders.forEach { folder ->
            if (scanner.deleteFile(folder.path)) {
                fileDao.deleteByPath(folder.path)
                count++
            }
        }
        return count
    }
}
