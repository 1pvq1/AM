package com.example.androidmaiden.core.experimental.time

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Implementation of [TimeProvider] using the system clock from kotlinx-datetime.
 * val now = kotlinx.datetime.Clock.System.now() //Unresolved reference 'System'.
 */
@OptIn(ExperimentalTime::class)
class SystemTimeProvider : TimeProvider {
    override fun now(): Instant {
        val now = Clock.System.now()
        return Instant.fromEpochMilliseconds(now.toEpochMilliseconds())
    }

    override fun nowMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}
