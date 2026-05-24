package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.repository.FileClearRepository
import com.example.androidmaiden.domain.model.CleanupResult
import com.example.androidmaiden.domain.model.CleanupStats
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing file cleanup operations like moving to trash and deleting duplicates.
 */
class FileClearViewModel(private val repository: FileClearRepository) : BaseViewModel() {

    /**
     * Flow of current cleanup statistics.
     */
    val cleanupStats: StateFlow<CleanupStats> = repository.cleanupStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CleanupStats())

    private val _cleanupResult = MutableStateFlow<CleanupResult?>(null)

    /**
     * Flow of the result of the last cleanup operation.
     */
    val cleanupResult: StateFlow<CleanupResult?> = _cleanupResult.asStateFlow()

    /**
     * Moves a file to the recycle bin.
     */
    fun moveToTrash(path: String) {
        viewModelScope.launch {
            val success = repository.moveToTrash(path)
            if (!success) {
                _cleanupResult.value = CleanupResult.Error("Failed to move to trash")
            }
        }
    }

    /**
     * Restores a file from the recycle bin.
     */
    fun restoreFromTrash(trashPath: String) {
        viewModelScope.launch {
            val success = repository.restoreFromTrash(trashPath)
            if (!success) {
                _cleanupResult.value = CleanupResult.Error("Failed to restore file")
            }
        }
    }

    /**
     * Permanently deletes a file from the recycle bin.
     */
    fun deletePermanently(trashPath: String) {
        viewModelScope.launch {
            val success = repository.deletePermanently(trashPath)
            if (!success) {
                _cleanupResult.value = CleanupResult.Error("Failed to delete permanently")
            }
        }
    }

    /**
     * Deletes all empty folders.
     */
    fun clearEmptyFolders() {
        viewModelScope.launch {
            _cleanupResult.value = CleanupResult.Loading()
            repository.clearEmptyFolders()
            _cleanupResult.value = CleanupResult.Success
        }
    }

    /**
     * Deletes all detected duplicate files.
     */
    fun clearDuplicates() {
        viewModelScope.launch {
            _cleanupResult.value = CleanupResult.Loading()
            val duplicates = cleanupStats.value.duplicateFiles
            val success = repository.deleteFiles(duplicates.map { it.path })
            if (success) {
                _cleanupResult.value = CleanupResult.Success
            } else {
                _cleanupResult.value = CleanupResult.Error("Failed to clear some duplicates")
            }
        }
    }

    /**
     * Resets the cleanup result state.
     */
    fun resetResult() {
        _cleanupResult.value = null
    }
}
