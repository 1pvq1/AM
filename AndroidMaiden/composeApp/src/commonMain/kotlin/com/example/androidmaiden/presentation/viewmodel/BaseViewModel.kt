package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Base ViewModel to provide unified state management and survival across configuration changes.
 * 
 * [IMPORTANT] This class is deprecated in favor of [com.example.androidmaiden.core.arch.MaidenViewModel].
 * New features should use the core base class.
 */
abstract class BaseViewModel : ViewModel() {

    // Common UI State that might be needed across multiple screens
    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    protected val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }
}
