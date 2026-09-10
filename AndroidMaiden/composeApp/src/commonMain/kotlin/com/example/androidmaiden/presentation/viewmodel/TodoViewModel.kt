package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.core.experimental.time.TimeProvider
import com.example.androidmaiden.data.repository.TodoRepository
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// commonMain/viewmodel/TodoViewModel.kt
@OptIn(kotlin.time.ExperimentalTime::class)
class TodoViewModel(
    private val repository: TodoRepository,
    private val timeProvider: TimeProvider,
) : BaseViewModel() {
    val items: StateFlow<List<TodoItem>> = repository.getAllTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    var newText by mutableStateOf("")
        private set

    var selectedPriority by mutableStateOf(TodoPriority.MEDIUM)
        private set

    var selectedCategory by mutableStateOf<String?>(null)
        private set

    var itemToEdit by mutableStateOf<TodoItem?>(null)
        private set

    fun onNewTextChanged(text: String) {
        newText = text
    }

    fun onPriorityChanged(priority: TodoPriority) {
        selectedPriority = priority
    }

    fun onCategoryChanged(category: String?) {
        selectedCategory = category
    }

    fun addItem() {
        if (newText.isNotBlank()) {
            viewModelScope.launch {
                val newItem = TodoItem(
                    text = newText,
                    isChecked = false,
                    category = selectedCategory,
                    priority = selectedPriority,
                    createdAt = timeProvider.now()
                )
                repository.insertTodo(newItem)
                newText = ""
            }
        }
    }

    fun updateItem(item: TodoItem, newText: String, category: String?, priority: TodoPriority) {
        viewModelScope.launch {
            repository.updateTodo(item.copy(text = newText, category = category, priority = priority))
            itemToEdit = null
        }
    }

    fun toggleChecked(item: TodoItem, checked: Boolean) {
        viewModelScope.launch {
            repository.updateTodo(item.copy(isChecked = checked))
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch {
            repository.deleteTodo(item)
        }
    }

    fun startEdit(item: TodoItem) {
        itemToEdit = item
    }

    fun cancelEdit() {
        itemToEdit = null
    }
}
