package com.example.androidmaiden.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * Formats a given epoch millisecond timestamp into a human-readable date-time string.
 * Example: "2023-10-27 10:30"
 */
@OptIn(ExperimentalTime::class)
fun formatDateTime(epochMillis: Long?): String {
    if (epochMillis == null) return "Unknown"
    // Use the fully qualified name to avoid conflict if any, or standard alias
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.year}-${local.monthNumber.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')} " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

/**
 * Formats a stable [kotlin.time.Instant] into a human-readable string.
 */
@OptIn(ExperimentalTime::class)
fun formatInstant(instant: kotlin.time.Instant?): String {
    if (instant == null) return "Unknown"
    return formatDateTime(instant.toEpochMilliseconds())
}

fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))

    return if (hours > 0) {
        "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    } else {
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
}


fun formatSize(size: Long?): String {
    if (size == null) return "Unknown"
    return if (size > 1024 * 1024) "${size / (1024 * 1024)} MB" else "${size / 1024} KB"
}
