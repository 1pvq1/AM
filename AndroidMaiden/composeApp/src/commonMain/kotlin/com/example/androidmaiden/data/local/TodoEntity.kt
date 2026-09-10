package com.example.androidmaiden.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidmaiden.domain.model.TodoItem
import com.example.androidmaiden.domain.model.TodoPriority
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
            createdAt = item.createdAt.toEpochMilliseconds()
        )
    }
}
