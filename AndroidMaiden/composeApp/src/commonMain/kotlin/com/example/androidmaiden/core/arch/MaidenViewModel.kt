package com.example.androidmaiden.core.arch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base ViewModel for the project to ensure consistent state management.
 */
abstract class MaidenViewModel<State>(initialState: State) : ViewModel() {
    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    /**
     * Common UI state flags that might be needed across multiple screens.
     */
    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    protected val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    protected fun updateState(reducer: (State) -> State) {
        _uiState.update(reducer)
    }

    fun clearError() {
        _error.value = null
    }
}
