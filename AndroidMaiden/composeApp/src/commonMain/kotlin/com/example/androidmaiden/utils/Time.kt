package com.example.androidmaiden.utils

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
    if (epochMillis == null) return "未知时间"
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${local.year}-${local.monthNumber.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')} " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
}
