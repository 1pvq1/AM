package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.viewModels.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

data class TodoItem(
    val id: Long,
    val text: String,
    val isChecked: Boolean
)

@Preview(showBackground = true)
@Composable
fun TodoPage(viewModel: TodoViewModel = remember { TodoViewModel() }) {
    val todoItems = viewModel.items
    val newTodoText = viewModel.newText
    val itemToEdit = viewModel.itemToEdit

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Todo List", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { viewModel.onNewTextChanged(it) },
                label = { Text("New Task") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = viewModel::addItem,
                enabled = newTodoText.isNotBlank()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(todoItems, key = { _, item -> item.id }) { _, item ->
                TodoListItem(
                    item = item,
                    onCheckedChange = { checked -> viewModel.toggleChecked(item, checked) },
                    onDelete = { viewModel.deleteItem(item) },
                    onEdit = { viewModel.startEdit(item) }
                )
                HorizontalDivider()
            }
        }
    }

    itemToEdit?.let { item ->
        EditTodoDialog(
            item = item,
            onDismiss = viewModel::cancelEdit,
            onSave = { newText -> viewModel.updateItem(item, newText) }
        )
    }
}

//@Preview
//@Composable
//fun TodoPage() {
//    val todoItems = remember { mutableStateListOf<TodoItem>() }
//    var newTodoText by remember { mutableStateOf("") }
//    var showEditDialog by remember { mutableStateOf(false) }
//    var itemToEdit by remember { mutableStateOf<TodoItem?>(null) }
//    var nextId by remember { mutableStateOf(0L) }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        Text("Todo List", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = newTodoText,
//                onValueChange = { newTodoText = it },
//                label = { Text("New Task") },
//                modifier = Modifier.weight(1f),
//                singleLine = true
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                onClick = {
//                    if (newTodoText.isNotBlank()) {
//                        todoItems.add(
//                            TodoItem(
//                                id = nextId++,
//                                text = newTodoText,
//                                isChecked = false
//                            )
//                        )
//                        newTodoText = ""
//                    }
//                },
//                enabled = newTodoText.isNotBlank()
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Add Task")
//            }
//        }
//
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            itemsIndexed(todoItems, key = { _, item -> item.id }) { index, item ->
//                TodoListItem(
//                    item = item,
//                    onCheckedChange = { isChecked ->
//                        todoItems[index] = item.copy(isChecked = isChecked)
//                    },
//                    onDelete = { todoItems.remove(item) },
//                    onEdit = {
//                        itemToEdit = item
//                        showEditDialog = true
//                    }
//                )
//                HorizontalDivider()
//            }
//        }
//    }
//
//    if (showEditDialog && itemToEdit != null) {
//        EditTodoDialog(
//            item = itemToEdit!!,
//            onDismiss = {
//                showEditDialog = false
//                itemToEdit = null
//            },
//            onSave = { newText ->
//                val index = todoItems.indexOf(itemToEdit)
//                if (index != -1) {
//                    todoItems[index] = itemToEdit!!.copy(text = newText)
//                }
//                showEditDialog = false
//                itemToEdit = null
//            }
//        )
//    }
//}

@Composable
fun TodoListItem(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onEdit)
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTodoDialogPreview() {
    EditTodoDialog(
        item = TodoItem(id = 1L, text = "Preview Task", isChecked = false),
        onDismiss = {},
        onSave = {}
    )
}
@Composable
fun EditTodoDialog(
    item: TodoItem,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember(item) { mutableStateOf(item.text) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onSave(text)
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

