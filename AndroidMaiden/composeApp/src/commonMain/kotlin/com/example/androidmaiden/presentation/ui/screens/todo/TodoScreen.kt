package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.viewmodel.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateful Entry Point for the Todo screen.
 */
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todoItems = viewModel.items
    val newTodoText = viewModel.newText
    val itemToEdit = viewModel.itemToEdit
    
    val windowSizeClass = LocalWindowSizeClass.current

    TodoAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        todoItems = todoItems,
        newTodoText = newTodoText,
        itemToEdit = itemToEdit,
        onNewTextChanged = viewModel::onNewTextChanged,
        onAddItem = viewModel::addItem,
        onToggleChecked = viewModel::toggleChecked,
        onDeleteItem = viewModel::deleteItem,
        onStartEdit = viewModel::startEdit,
        onCancelEdit = viewModel::cancelEdit,
        onUpdateItem = viewModel::updateItem
    )
}





/**
 * Preview for the entire Todo page.
 */
@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    MaterialTheme {
        TodoScreen(viewModel = TodoViewModel())
    }
}