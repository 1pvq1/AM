@file:OptIn(ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.ReminderSettings
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
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

// --- Visual Comparison Previews Across 3 Size Classes ---

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun TodoAdaptiveCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        androidx.compose.material3.Surface {
            TodoAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun TodoAdaptiveCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        androidx.compose.material3.Surface {
            TodoAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun TodoAdaptiveCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        androidx.compose.material3.Surface {
            TodoAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
        }
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

