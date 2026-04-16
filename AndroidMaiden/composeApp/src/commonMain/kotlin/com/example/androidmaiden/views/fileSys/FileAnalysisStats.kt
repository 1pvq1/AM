package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidmaiden.utils.formatSize
import com.example.androidmaiden.viewModels.FolderAnalysisStats
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Visual Pop-up for folder statistics.
 *
 * @param stats The statistics to display.
 * @param onDismiss Callback to dismiss the pop-up.
 */
@Composable
fun StatsPopUp(stats: FolderAnalysisStats, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Folder Analysis",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Files", value = stats.fileCount.toString())
                    StatItem(label = "Folders", value = stats.folderCount.toString())
                    StatItem(label = "Size", value = formatSize(stats.totalSize))
                }

                Spacer(Modifier.height(24.dp))

                if (stats.totalSize > 0) {
                    DistributionBar(distribution = stats.typeDistribution, totalSize = stats.totalSize)
                } else {
                    Text("No data to analyze", color = MaterialTheme.colorScheme.outline)
                }

                Spacer(Modifier.height(24.dp))

                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Close")
                }
            }
        }
    }
}

/**
 * A single statistic item with a label and value.
 */
@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
    }
}

/**
 * A horizontal bar showing the distribution of file types by size.
 */
@Composable
fun DistributionBar(distribution: Map<String, Long>, totalSize: Long) {
    val categoryColors = remember {
        mapOf(
            "Images" to Color(0xFF4CAF50),
            "Videos" to Color(0xFF2196F3),
            "Audio" to Color(0xFFE91E63),
            "Documents" to Color(0xFFFF9800),
            "APKs" to Color(0xFF3DDC84),
            "Archives" to Color(0xFF9C27B0),
            "Other" to Color(0xFF9E9E9E)
        )
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            distribution.forEach { (type, size) ->
                val weight = size.toFloat() / totalSize
                if (weight > 0.01f) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(weight)
                            .background(categoryColors[type] ?: categoryColors["Other"]!!)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Detailed Legend
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            distribution.filter { it.value > 0 }.forEach { (type, size) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(categoryColors[type] ?: categoryColors["Other"]!!)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "$type: ${formatSize(size)} (${(size * 100 / totalSize)}%)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
