package com.example.androidmaiden.core.util

import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

/**
 * Formats a given epoch millisecond timestamp into a human-readable date-time string.
 * Example: "2023-10-27 10:30"
 */
@OptIn(ExperimentalTime::class)
fun formatDateTime(epochMillis: Long?): String {
    if (epochMillis == null) return "Unknown"
    // Use the fully qualified name to avoid conflict if any, or standard alias
    val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(epochMillis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.year}-${local.month.number.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')} " +
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

/**
 * Formats an [Instant] to a date-only string.
 */
@OptIn(ExperimentalTime::class)
fun formatDate(instant: kotlin.time.Instant?): String {
    if (instant == null) return "None"
    val local = kotlinx.datetime.Instant.fromEpochMilliseconds(instant.toEpochMilliseconds())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.year}-${local.month.number.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')}"
}

/**
 * Formats an [Instant] to a time-only string.
 */
@OptIn(ExperimentalTime::class)
fun formatTime(instant: kotlin.time.Instant?): String {
    if (instant == null) return "None"
    val local = kotlinx.datetime.Instant.fromEpochMilliseconds(instant.toEpochMilliseconds())
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}

/**
 * Combines date millis with hour and minute to create an [Instant].
 */
@OptIn(ExperimentalTime::class)
fun combineToInstant(dateMillis: Long, hour: Int, minute: Int): kotlin.time.Instant {
    val localDateTime = kotlinx.datetime.Instant.fromEpochMilliseconds(dateMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val combined = kotlinx.datetime.LocalDateTime(
        localDateTime.year,
        localDateTime.month,
        localDateTime.dayOfMonth,
        hour,
        minute
    )
    return kotlin.time.Instant.fromEpochMilliseconds(
        combined.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )
}

/**
 * Returns the hour of the day for a given [Instant].
 */
@OptIn(ExperimentalTime::class)
fun getHour(instant: kotlin.time.Instant): Int {
    return kotlinx.datetime.Instant.fromEpochMilliseconds(instant.toEpochMilliseconds())
        .toLocalDateTime(TimeZone.currentSystemDefault()).hour
}

/**
 * Returns the minute of the hour for a given [Instant].
 */
@OptIn(ExperimentalTime::class)
fun getMinute(instant: kotlin.time.Instant): Int {
    return kotlinx.datetime.Instant.fromEpochMilliseconds(instant.toEpochMilliseconds())
        .toLocalDateTime(TimeZone.currentSystemDefault()).minute
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
