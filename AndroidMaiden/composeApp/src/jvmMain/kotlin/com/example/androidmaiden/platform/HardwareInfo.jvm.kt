package com.example.androidmaiden.platform

import com.example.androidmaiden.domain.hardware.model.*
import kotlin.random.Random

/**
 * JVM Desktop actual implementation supplying simulated system telemetry metrics 
 * mapping to standard Clean Architecture models.
 */
actual fun getPlatformSystemResources(context: Any?): SystemResources {
    // Simulated RAM memory states
    val totalBytes = 16L * 1024L * 1024L * 1024L // 16 GB
    val usedBytes = (6.2 * 1024.0 * 1024.0 * 1024.0).toLong() + Random.nextLong(200L * 1024L * 1024L)
    val ramState = RamState(
        usedBytes = usedBytes,
        totalBytes = totalBytes,
        freeBytes = totalBytes - usedBytes
    )

    // Simulated CPU Core loads
    val cpuCores = List(8) { id ->
        CpuCoreInfo(
            coreId = id,
            usagePercentage = 10f + Random.nextFloat() * 15f,
            frequencyMhz = 2400 + Random.nextInt(400)
        )
    }

    return SystemResources(
        cpuCores = cpuCores,
        ramState = ramState,
        batteryState = BatteryState(
            percentage = 95f,
            temperatureCelsius = 38.5f,
            voltageMv = 12000, // Typical laptop battery voltage
            isCharging = true,
            status = "Charging"
        ),
        timestamp = System.currentTimeMillis()
    )
}
