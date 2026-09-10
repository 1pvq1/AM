@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.domain.model

//import kotlinx.datetime.Instant
//Instant = Instant' is deprecated. This type is deprecated in favor of `kotlin.time.Instant`.
//import kotlin.time.ExperimentalTime
import kotlin.time.Instant

enum class TodoPriority {
    LOW, MEDIUM, HIGH
}

data class TodoItem(
    val id: Long = 0,
    val text: String,
    val isChecked: Boolean = false,
    val category: String? = null,
    val priority: TodoPriority = TodoPriority.MEDIUM,
    val dueDate: Instant? = null,
    val createdAt: Instant
)
