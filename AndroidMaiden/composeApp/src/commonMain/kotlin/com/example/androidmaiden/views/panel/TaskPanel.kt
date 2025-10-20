package com.example.androidmaiden.views.panel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Task(
    val id: Int,
    val title: String,
    var isDone: Boolean = false
)

@Preview
@Composable
fun PanelOfTask() {
    // 示例任务列表（后续可替换为 ViewModel 数据）
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "完成 Compose 布局"),
                Task(2, "优化对话框 UI"),
                Task(3, "编写任务区 MVP")
            )
        )
    }

    var newTaskTitle by remember { mutableStateOf("") }

    Spacer(Modifier.height(12.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedCard {
            Text(
                text = "今日任务 (${tasks.count { it.isDone }}/${tasks.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            tasks = inputNewTask(newTaskTitle, tasks)

            tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = { checked ->
                        tasks = tasks.map {
                            if (it.id == task.id) it.copy(isDone = checked) else it
                        }
                    }
                )

            }
        }
    }
}

@Composable
private fun inputNewTask(
    newTaskTitle: String,
    tasks: List<Task>
): List<Task> {
    var newTaskTitle1 = newTaskTitle
    var tasks1 = tasks
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = newTaskTitle1,
            onValueChange = { newTaskTitle1 = it },
            label = { Text("新任务") },
            modifier = Modifier.weight(1f).padding(5.dp)
        )
        Button(
//            content = Icon,
            onClick = {
                if (newTaskTitle1.isNotBlank()) {
                    val nextId = (tasks1.maxOfOrNull { it.id } ?: 0) + 1
                    tasks1 = tasks1 + Task(nextId, newTaskTitle1.trim())
                    newTaskTitle1 = "" // 清空输入框
                }
            }
        ) {
            Text("添加")
        }
    }
    return tasks1
}

@Composable
fun TaskItem(task: Task, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = onCheckedChange
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (task.isDone)
                        MaterialTheme.colorScheme.outline
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}