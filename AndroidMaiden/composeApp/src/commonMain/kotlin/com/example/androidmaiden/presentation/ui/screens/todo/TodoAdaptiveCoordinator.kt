@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
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
    selectedPriority: TodoPriority,
    selectedCategory: String?,
    itemToEdit: TodoItem?,
    onNewTextChanged: (String) -> Unit,
    onPriorityChanged: (TodoPriority) -> Unit,
    onCategoryChanged: (String?) -> Unit,
    onAddItem: () -> Unit,
    onToggleChecked: (TodoItem, Boolean) -> Unit,
    onDeleteItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onCancelEdit: () -> Unit,
    onUpdateItem: (TodoItem, String, String?, TodoPriority) -> Unit
) {
    val isWide = windowSizeClass.widthCategory != WindowSizeCategory.Compact

    TodoContent(
        isWide = isWide,
        todoItems = todoItems,
        newTodoText = newTodoText,
        selectedPriority = selectedPriority,
        selectedCategory = selectedCategory,
        itemToEdit = itemToEdit,
        onNewTextChanged = onNewTextChanged,
        onPriorityChanged = onPriorityChanged,
        onCategoryChanged = onCategoryChanged,
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
        TodoItem(1, "Buy milk", false, createdAt = kotlin.time.Instant.fromEpochMilliseconds(0)),
        TodoItem(2, "Learn Compose", true, category = "Study", priority = TodoPriority.HIGH, createdAt = kotlin.time.Instant.fromEpochMilliseconds(0))
    )
    TodoAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        todoItems = sampleTodos,
        newTodoText = "",
        selectedPriority = TodoPriority.MEDIUM,
        selectedCategory = null,
        itemToEdit = null,
        onNewTextChanged = { },
        onPriorityChanged = { },
        onCategoryChanged = { },
        onAddItem = { },
        onToggleChecked = { _, _ -> },
        onDeleteItem = { },
        onStartEdit = { },
        onCancelEdit = { },
        onUpdateItem = { _, _, _, _ -> }
    )
}
