package com.example.androidmaiden.presentation.ui.screens.hardware.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.hardware.model.CpuCoreInfo
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

/**
 * Composable that displays logical processor core layouts with usage stats and frequency details.
 *
 * This grid presents a detailed view of each available CPU core, including:
 * - Real-time load percentage with a visual progress bar.
 * - Active clock frequency in MHz.
 * - Dynamic color-coding based on core utilization.
 *
 * The layout uses a manually chunked row system to ensure compatibility within
 * scrollable parent containers.
 *
 * @param cores A list of [CpuCoreInfo] objects representing each logical processor core.
 * @param modifier Modifier to be applied to the outer card.
 */
@Composable
fun CpuCoreGrid(
    cores: List<CpuCoreInfo>,
    modifier: Modifier = Modifier,
) {
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
            // Header
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DeveloperBoard,
                    contentDescription = "Cores",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "PROCESSOR CORE THREADS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // LazyVerticalGrid doesn't work inside scrollable Column nicely without explicit height, 
            // but we can use simple flow layouts or standard non-lazy loops if cores are small,
            // or simply divide them manually in code, or use a Row/Column pattern.
            // Let's do a manually-structured layout of rows containing 2 cores each to avoid issues 
            // in outer scrollable columns.
            val chunkedCores = cores.chunked(2)
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                chunkedCores.forEach { rowCores ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowCores.forEach { core ->
                            CoreItem(
                                core = core,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Add placeholder if odd number of items
                        if (rowCores.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CpuCoreGridPreview() {
    AppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CpuCoreGrid(
                cores = listOf(
                    CpuCoreInfo(0, 45f, 1800),
                    CpuCoreInfo(1, 12f, 1200),
                    CpuCoreInfo(2, 88f, 2400),
                    CpuCoreInfo(3, 30f, 1600),
                    CpuCoreInfo(4, 5f, 800),
                    CpuCoreInfo(5, 100f, 2800),
                    CpuCoreInfo(6, 60f, 2000),
                    CpuCoreInfo(7, 25f, 1400)
                )
            )
        }
    }
}

@Composable
private fun CoreItem(
    core: CpuCoreInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Core #${core.coreId}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "${core.frequencyMhz} MHz",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            LinearProgressIndicator(
                progress = { core.usagePercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                strokeCap = StrokeCap.Round,
                color = when {
                    core.usagePercentage >= 85f -> MaterialTheme.colorScheme.error
                    core.usagePercentage >= 70f -> Color(0xFFFF9800)
                    else -> MaterialTheme.colorScheme.primary
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${core.usagePercentage.roundToInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}
