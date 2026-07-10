package com.example.androidmaiden.domain.hardware.repository

import com.example.androidmaiden.domain.hardware.model.*
import kotlinx.coroutines.flow.*

/**
 * Repository interface for managing hardware telemetry retrieval and simulation triggers.
 */
interface HardwareRepository {
    /**
     * Observes the system resources changes in real-time.
     * @param pollIntervalMs interval in milliseconds at which system status is polled.
     */
    fun observeSystemResources(pollIntervalMs: Long): Flow<SystemResources>

    /**
     * Triggers a simulated CPU stress test for a specific duration.
     * @param durationMs duration of the stress test in milliseconds.
     */
    fun triggerStressTest(durationMs: Long)

    /**
     * Observes whether the system is currently running a CPU stress test.
     */
    fun isStressTesting(): Flow<Boolean>

    /**
     * Cancels any active CPU stress test.
     */
    fun stopStressTest()
}
