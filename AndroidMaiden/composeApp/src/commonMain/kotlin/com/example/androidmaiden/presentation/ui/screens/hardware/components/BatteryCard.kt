package com.example.androidmaiden.presentation.ui.screens.hardware.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.hardware.model.BatteryState
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable displaying real-time battery voltage, charging status, and temperature parameters.
 *
 * This component provides a visual summary of the device's power system, including:
 * - Current charge percentage with a progress indicator.
 * - Charging status (e.g., Charging, Discharging).
 * - Real-time temperature with overheat warning threshold (>= 45°C).
 * - Precise battery voltage telemetry.
 *
 * @param batteryState The current state of the battery, or null if telemetry is unavailable.
 * @param modifier Modifier to be applied to the card.
 */
@Composable
fun BatteryCard(
    batteryState: BatteryState?,
    modifier: Modifier = Modifier
) {
    val level = batteryState?.percentage ?: 0f
    val temperature = batteryState?.temperatureCelsius ?: 0f
    val voltage = batteryState?.voltageMv ?: 0
    val isCharging = batteryState?.isCharging ?: false
    val status = batteryState?.status ?: "Unknown"

    // High temperature threshold warning
    val isOverheating = temperature >= 45.0f

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryStd,
                        contentDescription = "Battery",
                        tint = if (isOverheating) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "BATTERY CORE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                if (isOverheating) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.errorContainer,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Overheat",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "OVERHEAT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                } else {
                    Text(
                        text = status.uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isCharging) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Battery Progress percentage bar
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Capacity Charge",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${level.toInt()}%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                LinearProgressIndicator(
                    progress = { level / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    strokeCap = StrokeCap.Round,
                    color = if (level <= 15f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Specs Row (Temperature & Voltage)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Temperature
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isOverheating) MaterialTheme.colorScheme.error.copy(alpha = 0.5f) else Color.Transparent
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = "Temperature",
                            tint = if (isOverheating) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "TEMP",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            val tempStr = ((temperature * 10).toInt() / 10.0).toString()
                            Text(
                                text = "$tempStr °C",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOverheating) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }

                // Voltage
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Voltage",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "VOLTAGE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            val voltageV = voltage / 1000.0
                            val voltStr = ((voltageV * 1000).toInt() / 1000.0).toString()
                            Text(
                                text = "$voltStr V",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BatteryCardPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            BatteryCard(
                batteryState = BatteryState(
                    percentage = 85f,
                    temperatureCelsius = 32.5f,
                    voltageMv = 3850,
                    isCharging = true,
                    status = "Charging"
                )
            )
            BatteryCard(
                batteryState = BatteryState(
                    percentage = 12f,
                    temperatureCelsius = 46.2f,
                    voltageMv = 3600,
                    isCharging = false,
                    status = "Discharging"
                )
            )
        }
    }
}
