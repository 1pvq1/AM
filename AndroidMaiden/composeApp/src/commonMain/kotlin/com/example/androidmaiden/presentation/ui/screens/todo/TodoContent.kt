@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.ReminderFrequency
import com.example.androidmaiden.domain.model.ReminderSettings
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
import com.example.androidmaiden.presentation.ui.screens.todo.components.EditTodoDialog
import com.example.androidmaiden.presentation.ui.screens.todo.components.TodoListItem
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateless UI for the Todo screen.
 */
@Composable
fun TodoContent(
    isWide: Boolean,
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
    val contentModifier = if (isWide) {
        Modifier.fillMaxSize().padding(horizontal = 32.dp).widthIn(max = 800.dp)
    } else {
        Modifier.fillMaxSize()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = contentModifier.padding(16.dp)
        ) {
            Text(
                text = "Todo List",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input Area
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newTodoText,
                            onValueChange = onNewTextChanged,
                            label = { Text("New Task") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onAddItem,
                            enabled = newTodoText.isNotBlank()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Task")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Category (Simple text field for now)
                        OutlinedTextField(
                            value = selectedCategory ?: "",
                            onValueChange = { onCategoryChanged(it.ifBlank { null }) },
                            label = { Text("Category") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        // Priority Dropdown (Simplified)
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text("Priority: ${selectedPriority.name}")
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                TodoPriority.entries.forEach { priority ->
                                    DropdownMenuItem(
                                        text = { Text(priority.name) },
                                        onClick = {
                                            onPriorityChanged(priority)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Reminder Setting (Quick Toggle)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = reminderSettings != null,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    onSetDefaultReminder()
                                } else {
                                    onReminderSettingsChanged(null)
                                }
                            }
                        )
                        Text("Set reminder (10m)")
                        
                        if (reminderSettings != null) {
                            Spacer(Modifier.width(16.dp))
                            var freqExpanded by remember { mutableStateOf(false) }
                            Box {
                                TextButton(onClick = { freqExpanded = true }) {
                                    Text("Freq: ${reminderSettings.frequency.name}")
                                }
                                DropdownMenu(expanded = freqExpanded, onDismissRequest = { freqExpanded = false }) {
                                    ReminderFrequency.entries.forEach { freq ->
                                        DropdownMenuItem(
                                            text = { Text(freq.name) },
                                            onClick = {
                                                onReminderSettingsChanged(reminderSettings.copy(frequency = freq))
                                                freqExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Grouping could be added here later if needed
                itemsIndexed(todoItems, key = { _, item -> item.id }) { _, item ->
                    TodoListItem(
                        item = item,
                        onCheckedChange = { checked -> onToggleChecked(item, checked) },
                        onDelete = { onDeleteItem(item) },
                        onEdit = { onStartEdit(item) }
                    )
                }
            }
        }
    }

    itemToEdit?.let { item ->
        EditTodoDialog(
            item = item,
            currentTimeMillis = currentTimeMillis,
            onDismiss = onCancelEdit,
            onSave = onUpdateItem
        )
    }
}

@Preview
@Composable
fun TodoPagePreview() {
    TodoContent(
        isWide = false,
        todoItems = listOf(
            TodoItem(1L, "Task 1", false, createdAt = kotlin.time.Instant.fromEpochMilliseconds(0)),
            TodoItem(2L, "Task 2", true, category = "Work", priority = TodoPriority.HIGH, createdAt = kotlin.time.Instant.fromEpochMilliseconds(0))
        ),
        newTodoText = "",
        selectedPriority = TodoPriority.MEDIUM,
        selectedCategory = null,
        reminderSettings = null,
        itemToEdit = null,
        currentTimeMillis = 0,
        onNewTextChanged = {},
        onPriorityChanged = {},
        onCategoryChanged = {},
        onReminderSettingsChanged = {},
        onSetDefaultReminder = {},
        onAddItem = {},
        onToggleChecked = { _, _ -> },
        onDeleteItem = {},
        onStartEdit = {},
        onCancelEdit = {},
        onUpdateItem = { _, _, _, _, _ -> }
    )
}
