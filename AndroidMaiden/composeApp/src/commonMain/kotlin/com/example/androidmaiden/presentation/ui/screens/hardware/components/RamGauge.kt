package com.example.androidmaiden.presentation.ui.screens.hardware.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.hardware.model.RamState
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

/**
 * Composable that displays a circular glowing neon RAM utilization gauge.
 *
 * This component provides a high-visibility circular indicator of the device's
 * volatile memory usage. It features:
 * - An animated progress ring that changes color based on load thresholds (Safe, Warning, Critical).
 * - Real-time percentage display in the center.
 * - Detailed used/total memory stats in GB.
 *
 * @param ramState The current state of the RAM, including used and total bytes.
 * @param modifier Modifier to be applied to the gauge container.
 */
@Composable
fun RamGauge(
    ramState: RamState?,
    modifier: Modifier = Modifier,
) {
    val usagePercentage = ramState?.usagePercentage ?: 0f
    
    // Animate progress changes smoothly
    val animatedUsage by animateFloatAsState(
        targetValue = usagePercentage,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "ramUsagePercentage"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val errorColor = MaterialTheme.colorScheme.error
    val warningColor = Color(0xFFFF9800) // Neon orange

    // Determine color based on threshold load
    val gaugeColor = remember(animatedUsage) {
        when {
            animatedUsage >= 85f -> errorColor
            animatedUsage >= 70f -> warningColor
            else -> primaryColor
        }
    }

    // Convert bytes to readable GB string
    val usedGb = remember(ramState) {
        val bytes = ramState?.usedBytes ?: 0L
        (bytes / (1024.0 * 1024.0 * 1024.0) * 10.0).roundToInt() / 10.0
    }
    val totalGb = remember(ramState) {
        val bytes = ramState?.totalBytes ?: 0L
        (bytes / (1024.0 * 1024.0 * 1024.0) * 10.0).roundToInt() / 10.0
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(160.dp)
        ) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val radius = diameter / 2f
            
            // Draw background trace ring
            drawCircle(
                color = trackColor,
                radius = radius,
                style = Stroke(width = strokeWidth)
            )

            // Draw glowing path ring
            drawArc(
                color = gaugeColor,
                startAngle = -90f,
                sweepAngle = (animatedUsage / 100f) * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Dashboard info details in the center of the ring
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RAM",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = "${animatedUsage.roundToInt()}%",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = gaugeColor,
                    fontSize = 32.sp
                )
            )
            Text(
                text = "$usedGb GB / $totalGb GB",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            )
        }
    }
}

@Preview
@Composable
fun RamGaugePreview() {
    AppTheme {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RamGauge(
                ramState = RamState(
                    usedBytes = 4L * 1024 * 1024 * 1024,
                    totalBytes = 8L * 1024 * 1024 * 1024,
                    freeBytes = 4L * 1024 * 1024 * 1024
                ),
                modifier = Modifier.weight(1f)
            )
            RamGauge(
                ramState = RamState(
                    usedBytes = 7L * 1024 * 1024 * 1024,
                    totalBytes = 8L * 1024 * 1024 * 1024,
                    freeBytes = 1L * 1024 * 1024 * 1024
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
