package com.example.androidmaiden.views.panel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.screens.pages.TodoItem
import com.example.androidmaiden.viewModels.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PanelOfTask(viewModel: TodoViewModel = remember { TodoViewModel() }) {
    val tasks = viewModel.items
    val previewTasks = tasks.take(3) // Only show the first few tasks as a preview

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (tasks.isEmpty()) "Tasks" else "Tasks (${tasks.count { it.isChecked }}/${tasks.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (tasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tasks scheduled",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    previewTasks.forEach { task ->
                        TaskPreviewItem(
                            task = task,
                            onCheckedChange = { checked ->
                                viewModel.toggleChecked(task, checked)
                            }
                        )
                    }

                    if (tasks.size > 3) {
                        Text(
                            text = "... and ${tasks.size - 3} more tasks",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskPreviewItem(task: TodoItem, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = task.isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = task.text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (task.isChecked)
                MaterialTheme.colorScheme.outline
            else
                MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}
