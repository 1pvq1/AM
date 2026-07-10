package com.example.androidmaiden.domain.hardware.model

import com.example.androidmaiden.domain.hardware.model.*

/**
 * Aggregated domain entity that captures a single snapshot of all monitored hardware components.
 */
data class SystemResources(
    val cpuCores: List<CpuCoreInfo>,
    val ramState: RamState,
    val batteryState: BatteryState,
    val timestamp: Long
)
