package com.example.androidmaiden.viewModels

import androidx.compose.runtime.*
import com.example.androidmaiden.screens.pages.TodoItem

// commonMain/viewmodel/TodoViewModel.kt
class TodoViewModel {
    private val _items = mutableStateListOf<TodoItem>()
    val items: List<TodoItem> get() = _items

    var newText by mutableStateOf("")
        private set

    var itemToEdit by mutableStateOf<TodoItem?>(null)
        private set

    fun onNewTextChanged(text: String) {
        newText = text
    }

    fun addItem() {
        if (newText.isNotBlank()) {
            val nextId = (_items.maxOfOrNull { it.id } ?: 0L) + 1
            _items.add(TodoItem(id = nextId, text = newText, isChecked = false))
            newText = ""
        }
    }

    fun updateItem(item: TodoItem, newText: String) {
        val index = _items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _items[index] = item.copy(text = newText)
        }
        itemToEdit = null
    }

    fun toggleChecked(item: TodoItem, checked: Boolean) {
        val index = _items.indexOf(item)
        if (index != -1) {
            _items[index] = item.copy(isChecked = checked)
        }
    }

    fun deleteItem(item: TodoItem) {
        _items.remove(item)
    }

    fun startEdit(item: TodoItem) {
        itemToEdit = item
    }

    fun cancelEdit() {
        itemToEdit = null
    }
}