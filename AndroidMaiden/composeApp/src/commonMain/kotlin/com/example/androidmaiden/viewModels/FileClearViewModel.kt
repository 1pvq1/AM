package com.example.androidmaiden.viewModels

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.FileClearRepository
import com.example.androidmaiden.model.CleanupResult
import com.example.androidmaiden.model.CleanupStats
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FileClearViewModel(private val repository: FileClearRepository) : BaseViewModel() {

    val cleanupStats: StateFlow<CleanupStats> = repository.cleanupStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CleanupStats())

    private val _cleanupResult = MutableStateFlow<CleanupResult?>(null)
    val cleanupResult: StateFlow<CleanupResult?> = _cleanupResult.asStateFlow()

    fun moveToTrash(path: String) {
        viewModelScope.launch {
            val success = repository.moveToTrash(path)
            if (!success) {
                _cleanupResult.value = CleanupResult.Error("Failed to move to trash")
            }
        }
    }

    fun restoreFromTrash(trashPath: String) {
        viewModelScope.launch {
            val success = repository.restoreFromTrash(trashPath)
            if (!success) {
                _cleanupResult.value = CleanupResult.Error("Failed to restore file")
            }
        }
    }

    fun deletePermanently(trashPath: String) {
        viewModelScope.launch {
            val success = repository.deletePermanently(trashPath)
            if (!success) {
                _cleanupResult.value = CleanupResult.Error("Failed to delete permanently")
            }
        }
    }

    fun clearEmptyFolders() {
        viewModelScope.launch {
            _cleanupResult.value = CleanupResult.Loading()
            val count = repository.clearEmptyFolders()
            _cleanupResult.value = CleanupResult.Success
        }
    }

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

    fun resetResult() {
        _cleanupResult.value = null
    }
}
