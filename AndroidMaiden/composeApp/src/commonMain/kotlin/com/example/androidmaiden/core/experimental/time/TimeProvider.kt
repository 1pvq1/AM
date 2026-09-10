package com.example.androidmaiden.core.experimental.time

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Interface to provide time, allowing for abstraction of experimental APIs
 * and easier testing/mocking.
 */
@OptIn(ExperimentalTime::class)
interface TimeProvider {
    /**
     * Returns the current [Instant].
     */
    fun now(): Instant

    /**
     * Returns the current time in milliseconds since epoch.
     */
    fun nowMillis(): Long
}
