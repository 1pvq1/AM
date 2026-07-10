package com.example.androidmaiden.domain.hardware.model

/**
 * Data class representing real-time Battery status and specifications.
 */
data class BatteryState(
    val percentage: Float, // Range: 0.0 to 100.0
    val temperatureCelsius: Float,
    val voltageMv: Int,
    val isCharging: Boolean,
    val status: String // E.g., "Charging", "Discharging", "Full", "Unknown"
)
