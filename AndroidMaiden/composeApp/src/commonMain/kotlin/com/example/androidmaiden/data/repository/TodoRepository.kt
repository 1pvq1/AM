package com.example.androidmaiden.data.repository

import com.example.androidmaiden.core.experimental.job.BackgroundJobManager
import com.example.androidmaiden.core.experimental.time.TimeProvider
import com.example.androidmaiden.data.local.TodoDao
import com.example.androidmaiden.data.local.TodoEntity
import com.example.androidmaiden.domain.model.ReminderFrequency
import com.example.androidmaiden.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class TodoRepository(
    private val todoDao: TodoDao,
    private val jobManager: BackgroundJobManager,
    private val timeProvider: TimeProvider
) {
    fun getAllTodos(): Flow<List<TodoItem>> {
        return todoDao.getAllTodos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getTodoById(id: Long): TodoItem? {
        return todoDao.getTodoById(id)?.toDomain()
    }

    suspend fun insertTodo(item: TodoItem): Long {
        val id = todoDao.insertTodo(TodoEntity.fromDomain(item))
        scheduleReminder(item.copy(id = id))
        return id
    }

    suspend fun updateTodo(item: TodoItem) {
        todoDao.updateTodo(TodoEntity.fromDomain(item))
        scheduleReminder(item)
    }

    suspend fun deleteTodo(item: TodoItem) {
        todoDao.deleteTodo(TodoEntity.fromDomain(item))
        jobManager.cancelJob("todo_${item.id}")
    }

    suspend fun deleteTodoById(id: Long) {
        todoDao.deleteTodoById(id)
        jobManager.cancelJob("todo_$id")
    }

    private fun scheduleReminder(item: TodoItem) {
        val reminderId = "todo_${item.id}"
        val settings = item.reminderSettings

        if (settings == null || item.isChecked) {
            jobManager.cancelJob(reminderId)
            return
        }

        val now = timeProvider.nowMillis()
        val initialDelay = settings.time.toEpochMilliseconds() - now

        if (initialDelay < 0 && settings.frequency == ReminderFrequency.ONCE) {
            jobManager.cancelJob(reminderId)
            return
        }

        val safeDelay = initialDelay.coerceAtLeast(0)

        when (settings.frequency) {
            ReminderFrequency.ONCE -> {
                jobManager.scheduleNotification(reminderId, item.text, safeDelay)
            }
            ReminderFrequency.DAILY -> {
                val interval = 24 * 60 * 60 * 1000L
                jobManager.schedulePeriodicNotification(reminderId, item.text, interval, safeDelay)
            }
            ReminderFrequency.WEEKLY -> {
                val interval = 7 * 24 * 60 * 60 * 1000L
                jobManager.schedulePeriodicNotification(reminderId, item.text, interval, safeDelay)
            }
        }
    }
}
