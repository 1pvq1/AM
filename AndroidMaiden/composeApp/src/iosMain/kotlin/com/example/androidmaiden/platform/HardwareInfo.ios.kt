package com.example.androidmaiden.platform

import com.example.androidmaiden.domain.hardware.model.*
import kotlin.random.Random

/**
 * iOS actual implementation supplying simulated system telemetry metrics 
 * mapping to standard Clean Architecture models.
 */
actual fun getPlatformSystemResources(context: Any?): SystemResources {
    // Simulated RAM memory states
    val totalBytes = 6L * 1024L * 1024L * 1024L // 6 GB (iPhone spec)
    val usedBytes = (3.1 * 1024.0 * 1024.0 * 1024.0).toLong() + Random.nextLong(100L * 1024L * 1024L)
    val ramState = RamState(
        usedBytes = usedBytes,
        totalBytes = totalBytes,
        freeBytes = totalBytes - usedBytes
    )

    // Simulated CPU Core loads
    val cpuCores = List(6) { id ->
        CpuCoreInfo(
            coreId = id,
            usagePercentage = 8f + Random.nextFloat() * 12f,
            frequencyMhz = 2100 + Random.nextInt(300)
        )
    }

    return SystemResources(
        cpuCores = cpuCores,
        ramState = ramState,
        batteryState = BatteryState(
            percentage = 72f,
            temperatureCelsius = 31.0f,
            voltageMv = 3850,
            isCharging = false,
            status = "Discharging"
        ),
        timestamp = System.currentTimeMillis()
    )
}
