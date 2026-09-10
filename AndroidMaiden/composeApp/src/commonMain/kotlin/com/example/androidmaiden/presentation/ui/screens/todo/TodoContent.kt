@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
import com.example.androidmaiden.core.util.formatInstant
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
            onDismiss = onCancelEdit,
            onSave = onUpdateItem
        )
    }
}

/**
 * Composable for a single todo item in the list.
 */
@Composable
fun TodoListItem(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = onCheckedChange
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .clickable(onClick = onEdit)
                    ) {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item.category?.let {
                            SuggestionChip(
                                onClick = {},
                                label = { Text(it, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                        
                        val priorityColor = when (item.priority) {
                            TodoPriority.HIGH -> MaterialTheme.colorScheme.error
                            TodoPriority.MEDIUM -> MaterialTheme.colorScheme.primary
                            TodoPriority.LOW -> MaterialTheme.colorScheme.secondary
                        }
                        
                        Text(
                            text = item.priority.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = priorityColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 48.dp)
                ) {
                    Text(
                        text = "Details: ${item.text}\nCreated: ${formatInstant(item.createdAt)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                            Spacer(Modifier.width(4.dp))
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dialog for editing an existing todo item.
 */
@Composable
fun EditTodoDialog(
    item: TodoItem,
    onDismiss: () -> Unit,
    onSave: (TodoItem, String, String?, TodoPriority) -> Unit
) {
    var text by remember(item) { mutableStateOf(item.text) }
    var category by remember(item) { mutableStateOf(item.category ?: "") }
    var priority by remember(item) { mutableStateOf(item.priority) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text("Priority")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TodoPriority.entries.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSave(item, text, category.ifBlank { null }, priority)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Light theme preview for the todo list item.
 */
@Preview
@Composable
fun TodoListItemPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            TodoListItem(
                item = TodoItem(
                    id = 1L,
                    text = "This is a very long todo item that should be scrollable horizontally to see the full content",
                    isChecked = false,
                    createdAt = kotlin.time.Instant.fromEpochMilliseconds(0)
                ),
                onCheckedChange = {},
                onDelete = {},
                onEdit = {}
            )
        }
    }
}

/**
 * Dark theme preview for the todo list item.
 */
@Preview
@Composable
fun TodoListItemPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface {
            TodoListItem(
                item = TodoItem(
                    id = 1L,
                    text = "This is a very long todo item that should be scrollable horizontally to see the full content",
                    isChecked = true,
                    createdAt = kotlin.time.Instant.fromEpochMilliseconds(0)
                ),
                onCheckedChange = {},
                onDelete = {},
                onEdit = {}
            )
        }
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
        itemToEdit = null,
        onNewTextChanged = {},
        onPriorityChanged = {},
        onCategoryChanged = {},
        onAddItem = {},
        onToggleChecked = { _, _ -> },
        onDeleteItem = {},
        onStartEdit = {},
        onCancelEdit = {},
        onUpdateItem = { _, _, _, _ -> }
    )
}
