package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass

/**
 * The Adaptive Coordinator for the Todo screen.
 */
@Composable
fun TodoAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
    todoItems: List<TodoItem>,
    newTodoText: String,
    itemToEdit: TodoItem?,
    onNewTextChanged: (String) -> Unit,
    onAddItem: () -> Unit,
    onToggleChecked: (TodoItem, Boolean) -> Unit,
    onDeleteItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onCancelEdit: () -> Unit,
    onUpdateItem: (TodoItem, String) -> Unit
) {
    val isWide = windowSizeClass.widthCategory != WindowSizeCategory.Compact

    TodoContent(
        isWide = isWide,
        todoItems = todoItems,
        newTodoText = newTodoText,
        itemToEdit = itemToEdit,
        onNewTextChanged = onNewTextChanged,
        onAddItem = onAddItem,
        onToggleChecked = onToggleChecked,
        onDeleteItem = onDeleteItem,
        onStartEdit = onStartEdit,
        onCancelEdit = onCancelEdit,
        onUpdateItem = onUpdateItem
    )
}
