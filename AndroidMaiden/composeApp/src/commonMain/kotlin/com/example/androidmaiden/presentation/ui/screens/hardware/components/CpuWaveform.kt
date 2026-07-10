package com.example.androidmaiden.presentation.ui.screens.hardware.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

/**
 * Composable drawing an animated cyberpunk/holographic CPU waveform graph using Compose Canvas.
 *
 * This component visualizes the historical average CPU load across all cores.
 * It features:
 * - A grid-based background system for scale reference.
 * - A smooth cubic bezier path representing historical data points.
 * - A gradient-filled area beneath the wave for improved visibility.
 * - A glowing neon pulse animation that travels along the wave path.
 *
 * @param history A list of recent CPU load percentages (0.0 to 100.0) to display.
 * @param modifier Modifier to be applied to the chart container.
 */
@Composable
fun CpuWaveform(
    history: List<Float>,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    
    // Pulse animation factor for the neon outline glow effect
    val infiniteTransition = rememberInfiniteTransition(label = "waveGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Info
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
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Waveform",
                        tint = primaryColor
                    )
                    Text(
                        text = "CPU CYCLE MONITOR",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Show current average CPU usage
                val currentLoad = history.lastOrNull() ?: 0f
                Text(
                    text = "${currentLoad.roundToInt()}% LOAD",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                )
            }

            // Canvas Box containing chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val width = size.width
                    val height = size.height

                    // 1. Draw Grid System Background
                    val horizontalLines = 4
                    val verticalLines = 10
                    for (i in 0..horizontalLines) {
                        val y = (height / horizontalLines) * i
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    for (i in 0..verticalLines) {
                        val x = (width / verticalLines) * i
                        drawLine(
                            color = gridColor,
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // 2. Draw Waveform Path
                    if (history.size >= 2) {
                        val pointsCount = history.size
                        val stepX = width / (pointsCount - 1)
                        val path = Path()
                        val fillPath = Path()

                        history.forEachIndexed { index, value ->
                            val x = stepX * index
                            // Normalize usage load: 0% is at height, 100% is at 0 (top)
                            val y = height - ((value / 100f) * height)

                            if (index == 0) {
                                path.moveTo(x, y)
                                fillPath.moveTo(x, height)
                                fillPath.lineTo(x, y)
                            } else {
                                // Draw smooth curves (cubic bezier approximation)
                                val prevX = stepX * (index - 1)
                                val prevY = height - ((history[index - 1] / 100f) * height)
                                val controlX1 = prevX + (stepX / 2f)
                                val controlY1 = prevY
                                val controlX2 = prevX + (stepX / 2f)
                                val controlY2 = y
                                path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                                fillPath.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                            }
                            if (index == history.lastIndex) {
                                fillPath.lineTo(x, height)
                                fillPath.close()
                            }
                        }

                        // 3. Draw gradient area fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )

                        // 4. Draw primary wave outline stroke
                        drawPath(
                            path = path,
                            color = primaryColor,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // 5. Draw neon glowing pulse trace overlay
                        drawPath(
                            path = path,
                            color = primaryColor.copy(alpha = glowAlpha),
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CpuWaveformPreview() {
    AppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CpuWaveform(
                history = listOf(10f, 25f, 40f, 35f, 50f, 80f, 75f, 60f, 45f, 30f, 20f, 15f, 25f, 40f, 60f, 90f, 95f, 85f, 70f, 50f)
            )
        }
    }
}
