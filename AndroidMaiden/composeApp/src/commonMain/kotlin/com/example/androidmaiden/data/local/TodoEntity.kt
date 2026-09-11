package com.example.androidmaiden.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidmaiden.domain.model.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isChecked: Boolean,
    val category: String?,
    val priority: String, // Store enum as String
    val dueDate: Long?,   // Store Instant as Long (epoch millis)
    val reminderTime: Long?,
    val reminderFrequency: String?,
    val createdAt: Long,   // Store Instant as Long (epoch millis)
) {
    @OptIn(ExperimentalTime::class)
    fun toDomain(): TodoItem = TodoItem(
        id = id,
        text = text,
        isChecked = isChecked,
        category = category,
        priority = TodoPriority.valueOf(priority),
        dueDate = dueDate?.let { Instant.fromEpochMilliseconds(it) },
        reminderSettings = if (reminderTime != null && reminderFrequency != null) {
            ReminderSettings(
                time = Instant.fromEpochMilliseconds(reminderTime),
                frequency = ReminderFrequency.valueOf(reminderFrequency)
            )
        } else null,
        createdAt = Instant.fromEpochMilliseconds(createdAt)
    )

    companion object {
        @OptIn(ExperimentalTime::class)
        fun fromDomain(item: TodoItem): TodoEntity = TodoEntity(
            id = item.id,
            text = item.text,
            isChecked = item.isChecked,
            category = item.category,
            priority = item.priority.name,
            dueDate = item.dueDate?.toEpochMilliseconds(),
            reminderTime = item.reminderSettings?.time?.toEpochMilliseconds(),
            reminderFrequency = item.reminderSettings?.frequency?.name,
            createdAt = item.createdAt.toEpochMilliseconds()
        )
    }
}
