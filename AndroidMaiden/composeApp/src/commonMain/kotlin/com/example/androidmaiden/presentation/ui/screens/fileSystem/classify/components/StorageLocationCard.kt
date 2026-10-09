package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.model.StorageLocationInfo
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Visual card component presenting detected storage space details (Internal Storage vs External SD Card).
 *
 * @param info The [StorageLocationInfo] model containing space capacity and metrics.
 * @param modifier Optional modifier for styling.
 */
@Composable
fun StorageLocationCard(
    info: StorageLocationInfo,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = if (info.isExternalRemovable) Icons.Default.SdCard else Icons.Default.Smartphone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )

                    Column {
                        Text(
                            text = info.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = info.path,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    color = if (info.isExternalRemovable) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = if (info.isExternalRemovable) "External SD" else "Internal",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (info.isExternalRemovable) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (info.totalBytes > 0) {
                val progressFraction = (info.usedPercentage / 100f).coerceIn(0f, 1f)

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    color = when {
                        progressFraction > 0.9f -> MaterialTheme.colorScheme.error
                        progressFraction > 0.75f -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    },
                    trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Used: ${formatStorageBytes(info.usedBytes)} / ${formatStorageBytes(info.totalBytes)} (${info.usedPercentage.toInt()}%)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Free: ${formatStorageBytes(info.freeBytes)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStorageLocationCard() {
    val info = StorageLocationInfo(
        name = "Internal Storage",
        path = "/storage/emulated/0",
        isExternalRemovable = false,
        totalBytes = 128_000_000_000L,
        usedBytes = 42_000_000_000L,
        freeBytes = 86_000_000_000L
    )
    StorageLocationCard(info)
}

/**
 * Formats byte capacity into human-readable B/KB/MB/GB strings.
 */
private fun formatStorageBytes(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val gb = bytes.toDouble() / (1024.0 * 1024.0 * 1024.0)
    if (gb >= 1.0) return "${(gb * 10).toInt() / 10.0} GB"
    val mb = bytes.toDouble() / (1024.0 * 1024.0)
    if (mb >= 1.0) return "${mb.toInt()} MB"
    val kb = bytes.toDouble() / 1024.0
    return "${kb.toInt()} KB"
}
