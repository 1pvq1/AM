@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.domain.model

//import kotlinx.datetime.Instant
//Instant = Instant' is deprecated. This type is deprecated in favor of `kotlin.time.Instant`.
//import kotlin.time.ExperimentalTime
import kotlin.time.Instant

enum class TodoPriority {
    LOW, MEDIUM, HIGH
}

enum class ReminderFrequency {
    ONCE, DAILY, WEEKLY
}

data class ReminderSettings(
    val time: Instant,
    val frequency: ReminderFrequency
)

data class TodoItem(
    val id: Long = 0,
    val text: String,
    val isChecked: Boolean = false,
    val category: String? = null,
    val priority: TodoPriority = TodoPriority.MEDIUM,
    val dueDate: Instant? = null,
    val reminderSettings: ReminderSettings? = null,
    val createdAt: Instant
)
