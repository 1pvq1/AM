@file:OptIn(ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.ReminderSettings
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
    reminderSettings: ReminderSettings?,
    itemToEdit: TodoItem?,
    currentTimeMillis: Long,
    onNewTextChanged: (String) -> Unit,
    onPriorityChanged: (TodoPriority) -> Unit,
    onCategoryChanged: (String?) -> Unit,
    onReminderSettingsChanged: (ReminderSettings?) -> Unit,
    onSetDefaultReminder: () -> Unit,
    onAddItem: () -> Unit,
    onToggleChecked: (TodoItem, Boolean) -> Unit,
    onDeleteItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onCancelEdit: () -> Unit,
    onUpdateItem: (TodoItem, String, String?, TodoPriority, ReminderSettings?) -> Unit
) {
    val isWide = windowSizeClass.widthCategory != WindowSizeCategory.Compact

    TodoContent(
        isWide = isWide,
        todoItems = todoItems,
        newTodoText = newTodoText,
        selectedPriority = selectedPriority,
        selectedCategory = selectedCategory,
        reminderSettings = reminderSettings,
        itemToEdit = itemToEdit,
        currentTimeMillis = currentTimeMillis,
        onNewTextChanged = onNewTextChanged,
        onPriorityChanged = onPriorityChanged,
        onCategoryChanged = onCategoryChanged,
        onReminderSettingsChanged = onReminderSettingsChanged,
        onSetDefaultReminder = onSetDefaultReminder,
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
        TodoItem(1, "Buy milk", false, createdAt = Instant.fromEpochMilliseconds(0)),
        TodoItem(2, "Learn Compose", true, category = "Study", priority = TodoPriority.HIGH, createdAt = Instant.fromEpochMilliseconds(0))
    )
    TodoAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        todoItems = sampleTodos,
        newTodoText = "",
        selectedPriority = TodoPriority.MEDIUM,
        selectedCategory = null,
        reminderSettings = null,
        itemToEdit = null,
        onNewTextChanged = { },
        onPriorityChanged = { },
        onCategoryChanged = { },
        onReminderSettingsChanged = { },
        onSetDefaultReminder = { },
        onAddItem = { },
        onToggleChecked = { _, _ -> },
        onDeleteItem = { },
        onStartEdit = { },
        onCancelEdit = { },
        onUpdateItem = { _, _, _, _, _ -> },
        currentTimeMillis = 0L
    )
}
