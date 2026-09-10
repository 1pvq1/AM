package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.viewmodel.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateful Entry Point for the Todo screen.
 */
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todoItems by viewModel.items.collectAsState()
    val newTodoText = viewModel.newText
    val selectedPriority = viewModel.selectedPriority
    val selectedCategory = viewModel.selectedCategory
    val itemToEdit = viewModel.itemToEdit
    
    val windowSizeClass = LocalWindowSizeClass.current

    TodoAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        todoItems = todoItems,
        newTodoText = newTodoText,
        selectedPriority = selectedPriority,
        selectedCategory = selectedCategory,
        itemToEdit = itemToEdit,
        onNewTextChanged = viewModel::onNewTextChanged,
        onPriorityChanged = viewModel::onPriorityChanged,
        onCategoryChanged = viewModel::onCategoryChanged,
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
 * (Commented out as TodoViewModel now requires a repository)
 */
/*
@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    MaterialTheme {
        TodoScreen(viewModel = TodoViewModel())
    }
}
*/
