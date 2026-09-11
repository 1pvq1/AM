package com.example.androidmaiden.core.experimental.time

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


/**
 * Implementation of [TimeProvider] using the system clock from kotlinx-datetime.
 * kotlinx.datetime.Clock.System.now() //Unresolved reference 'System'.
 */
@OptIn(ExperimentalTime::class)
class SystemTimeProvider : TimeProvider {
    override fun now(): Instant {
        val nowMillis = Clock.System.now().toEpochMilliseconds()
        return Instant.fromEpochMilliseconds(nowMillis)
    }

    override fun nowMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    override fun nowPlus(offsetMillis: Long): Instant {
        val futureMillis = nowMillis() + offsetMillis
        return Instant.fromEpochMilliseconds(futureMillis)
    }
}
