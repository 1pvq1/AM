package com.example.androidmaiden.domain.hardware.model

/**
 * Data class representing real-time RAM usage statistics.
 */
data class RamState(
    val usedBytes: Long,
    val totalBytes: Long,
    val freeBytes: Long
) {
    /**
     * Calculates the memory usage percentage.
     */
    val usagePercentage: Float
        get() = if (totalBytes > 0) (usedBytes.toFloat() / totalBytes * 100f) else 0f
}
