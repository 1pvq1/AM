@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.todo.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.core.util.*
import com.example.androidmaiden.core.experimental.ui.*

/**
 * Dialog for editing an existing Todo item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoDialog(
    item: TodoItem,
    currentTimeMillis: Long,
    onDismiss: () -> Unit,
    onSave: (TodoItem, String, String?, TodoPriority, ReminderSettings?) -> Unit,
) {
    var text by remember(item) { mutableStateOf(item.text) }
    var category by remember(item) { mutableStateOf(item.category ?: "") }
    var priority by remember(item) { mutableStateOf(item.priority) }
    var reminder by remember(item) { mutableStateOf(item.reminderSettings) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = reminder?.time?.toEpochMilliseconds() ?: currentTimeMillis
    )
    val timePickerState = rememberTimePickerState(
        initialHour = reminder?.time?.let { getHour(it) } ?: 12,
        initialMinute = reminder?.time?.let { getMinute(it) } ?: 0
    )
    
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

                HorizontalDivider()
                Text("Reminder")
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(
                        checked = reminder != null,
                        onCheckedChange = { checked ->
                            reminder = if (checked) {
                                ReminderSettings(
                                    time = item.reminderSettings?.time ?: kotlin.time.Instant.fromEpochMilliseconds(currentTimeMillis),
                                    frequency = item.reminderSettings?.frequency ?: ReminderFrequency.ONCE
                                )
                            } else {
                                null
                            }
                        }
                    )
                    Text("Enable reminder")
                }
                
                if (reminder != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(formatDate(reminder!!.time))
                        }
                        OutlinedButton(
                            onClick = { showTimePicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(formatTime(reminder!!.time))
                        }
                    }

                    var freqExpanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { freqExpanded = true }) {
                            Text("Frequency: ${reminder!!.frequency.name}")
                        }
                        DropdownMenu(expanded = freqExpanded, onDismissRequest = { freqExpanded = false }) {
                            ReminderFrequency.entries.forEach { freq ->
                                DropdownMenuItem(
                                    text = { Text(freq.name) },
                                    onClick = {
                                        reminder = reminder!!.copy(frequency = freq)
                                        freqExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSave(item, text, category.ifBlank { null }, priority, reminder)
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

    if (showDatePicker) {
        MaidenDatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis ?: return@TextButton
                    val hour = reminder?.time?.let { getHour(it) } ?: 12
                    val minute = reminder?.time?.let { getMinute(it) } ?: 0
                    
                    reminder = reminder!!.copy(
                        time = combineToInstant(selectedDate, hour, minute)
                    )
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        MaidenTimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val currentMillis = reminder!!.time.toEpochMilliseconds()
                    reminder = reminder!!.copy(
                        time = combineToInstant(currentMillis, timePickerState.hour, timePickerState.minute)
                    )
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}
