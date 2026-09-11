@file:OptIn(ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import kotlin.time.ExperimentalTime

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

                        if (item.reminderSettings != null) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = "Reminder set",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
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

@Preview
@Composable
fun TodoListItemPreview(){
    TodoListItem(
        item = TODO(),
        onCheckedChange = TODO(),
        onDelete = TODO(),
        onEdit = TODO()
    )
}