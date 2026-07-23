package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview(name = "Compact", showBackground = true)
@Composable
fun TodoAdaptiveCoordinatorCompactPreview() {
    AppTheme {
        TodoAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
    }
}

@Preview(name = "Wide", showBackground = true)
@Composable
fun TodoAdaptiveCoordinatorWidePreview() {
    AppTheme {
        TodoAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
    }
}

@Composable
private fun TodoAdaptiveCoordinatorPreviewHelper(widthCategory: WindowSizeCategory) {
    val sampleTodos = listOf(
        TodoItem(1, "Buy milk", false),
        TodoItem(2, "Learn Compose", true)
    )
    TodoAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        todoItems = sampleTodos,
        newTodoText = "",
        itemToEdit = null,
        onNewTextChanged = { },
        onAddItem = { },
        onToggleChecked = { _, _ -> },
        onDeleteItem = { },
        onStartEdit = { },
        onCancelEdit = { },
        onUpdateItem = { _, _ -> }
    )
}
