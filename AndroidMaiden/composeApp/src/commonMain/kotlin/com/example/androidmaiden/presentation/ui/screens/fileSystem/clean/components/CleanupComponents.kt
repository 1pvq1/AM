package com.example.androidmaiden.presentation.ui.screens.fileSystem.clean.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.CleanupStats
import com.example.androidmaiden.domain.model.TrashRecord
import com.example.androidmaiden.core.util.formatSize
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Card summarizing the total space that can be reclaimed.
 *
 * @param stats The cleanup statistics containing reclaimable space info.
 */
@Composable
fun CleanupSummaryCard(stats: CleanupStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Total Removable Space", style = MaterialTheme.typography.labelLarge)
            Text(
                text = formatSize(stats.totalRedundantSize + stats.totalTrashSize),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Card for a specific cleanup action (e.g., clearing duplicates).
 *
 * @param title The title of the cleanup action.
 * @param subtitle A descriptive subtitle, often showing the number of files found.
 * @param icon The icon representing the action.
 * @param onClick Callback when the "Clean" button is clicked.
 */
@Composable
fun CleanupActionCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, null) },
        trailingContent = {
            Button(onClick = onClick) {
                Text("Clean")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Item representing a file in the recycle bin.
 *
 * @param entry The trash record data.
 * @param onRestore Callback to restore the item.
 * @param onDelete Callback to delete the item permanently.
 */
@Composable
fun TrashItemRow(entry: TrashRecord, onRestore: () -> Unit, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(entry.fileName) },
        supportingContent = { Text("Deleted from: ${entry.originalPath}") },
        leadingContent = { Icon(Icons.Default.DeleteOutline, null) },
        trailingContent = {
            Row {
                IconButton(onClick = onRestore) {
                    Icon(Icons.Default.Restore, contentDescription = "Restore")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteForever, contentDescription = "Delete Permanently")
                }
            }
        }
    )
}

/**
 * Light theme preview for the cleanup summary card.
 */
@Preview
@Composable
fun CleanupSummaryCardLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        CleanupSummaryCard(stats = CleanupStats())
    }
}

/**
 * Dark theme preview for the trash item row.
 */
@Preview
@Composable
fun TrashItemRowDarkPreview() {
    val sampleEntry = TrashRecord(
        originalPath = "/storage/emulated/0/Documents",
        trashPath = "/storage/emulated/0/.trash/old_document.pdf",
        deletedAt = 0,
        fileName = "old_document.pdf",
        size = 1024 * 50,
        isDirectory = false
    )
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            TrashItemRow(entry = sampleEntry, onRestore = {}, onDelete = {})
        }
    }
}
