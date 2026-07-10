package com.example.androidmaiden.domain.hardware.model

/**
 * Data class representing real-time processor core load and frequency statistics.
 */
data class CpuCoreInfo(
    val coreId: Int,
    val usagePercentage: Float, // Range: 0.0 to 100.0
    val frequencyMhz: Int
)
