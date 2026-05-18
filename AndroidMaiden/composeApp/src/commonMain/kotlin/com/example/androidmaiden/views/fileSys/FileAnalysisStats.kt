package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidmaiden.ui.theme.core.*
import com.example.androidmaiden.ui.theme.AppTheme
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
            modifier = Modifier.fillMaxWidth().padding(16.dp), shape = MaterialTheme.shapes.large
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
                    DistributionBar(
                        distribution = stats.typeDistribution, totalSize = stats.totalSize
                    )
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
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * A horizontal bar showing the distribution of file types by size.
 */
@Composable
fun DistributionBar(distribution: Map<String, Long>, totalSize: Long) {
    val fileTypeColors = LocalFileTypeColors.current
    val categoryColors = remember(fileTypeColors) {
        mapOf(
            "Images" to fileTypeColors.image,
            "Videos" to fileTypeColors.video,
            "Audio" to fileTypeColors.audio,
            "Documents" to fileTypeColors.document,
            "APKs" to fileTypeColors.apk,
            "Archives" to fileTypeColors.archive,
            "Other" to fileTypeColors.other
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().height(16.dp).clip(MaterialTheme.shapes.small)
        ) {
            distribution.forEach { (type, size) ->
                val weight = size.toFloat() / totalSize
                if (weight > 0.01f) {
                    Box(
                        modifier = Modifier.fillMaxHeight().weight(weight)
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
                        modifier = Modifier.size(12.dp).clip(MaterialTheme.shapes.extraSmall)
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


private val SampleFolderAnalysisStats = FolderAnalysisStats(
    typeDistribution = mapOf(
        "Images" to 1024L * 1024 * 50,
        "Videos" to 1024L * 1024 * 200,
        "Documents" to 1024L * 1024 * 10,
        "Other" to 1024L * 1024 * 5
    ), totalSize = 1024L * 1024 * 265, fileCount = 124, folderCount = 12
)

@Preview(showBackground = true)
@Composable
fun StatsPopUpPreview() {
    AppTheme {
        StatsPopUp(
            stats = SampleFolderAnalysisStats, onDismiss = {})
    }
}

@Preview(showBackground = true)
@Composable
fun StatItemPreview() {
    AppTheme {
        StatItem(label = "Files", value = "124")
    }
}

@Preview
@Composable
fun DistributionBarPreview() {
    AppTheme {
        DistributionBar(
            distribution = SampleFolderAnalysisStats.typeDistribution,
            totalSize = SampleFolderAnalysisStats.totalSize,
        )
    }
}
