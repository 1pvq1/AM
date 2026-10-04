package com.example.androidmaiden.presentation.ui.features.fileSys

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.icons.*
import com.example.androidmaiden.core.util.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Bottom sheet content for file/folder actions and Android OS architectural analysis info.
 *
 * @param node The file system node to perform actions on.
 * @param onEnterClick Callback for opening a folder.
 * @param onRenameClick Callback for renaming the node.
 * @param onDeleteClick Callback for deleting the node.
 */
@Composable
fun FileActionSheetContent(
    node: FileSysNode,
    onEnterClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        ListItem(
            headlineContent = { Text(node.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            supportingContent = { Text(node.path ?: "N/A", style = MaterialTheme.typography.bodySmall) },
            leadingContent = {
                Icon(
                    imageVector = if (node.isFolder) folderIcon(node.folderType) else fileIcon(node.name),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        HorizontalDivider()

        if (node.isFolder) {
            ListItem(
                headlineContent = { Text("Open Folder") },
                leadingContent = { Icon(Icons.Default.FolderOpen, contentDescription = "Open Folder") },
                modifier = Modifier.clickable { onEnterClick() }
            )
        }

        ListItem(
            headlineContent = { Text("Rename") },
            leadingContent = { Icon(Icons.Default.Edit, contentDescription = "Rename") },
            modifier = Modifier.clickable { onRenameClick() }
        )
        ListItem(
            headlineContent = { Text("Delete", color = MaterialTheme.colorScheme.error) },
            leadingContent = { Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error) },
            modifier = Modifier.clickable { onDeleteClick() }
        )

        HorizontalDivider()

        // Architectural Description Section
        if (node.description.isNotBlank() && !node.description.startsWith("Path:")) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Android Architecture Insights",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Analysis Info Section
        Text(
            text = "Item Details",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        DetailRow("Size", node.size?.let { formatSize(it) } ?: "N/A")
        DetailRow("Type", if (node.isFolder) "Folder" else "File")
        DetailRow("Last Modified", node.lastModified?.let { formatDateTime(it) } ?: "Unknown")
    }
}

/**
 * A simple row displaying a label and a value.
 */
@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * Preview for the file action sheet content.
 */
@Preview
@Composable
fun FileActionSheetContentPreview() {
    val fileSys = FileSysNode(
        name = "Documents",
        nodeType = NodeType.FOLDER,
        folderType = FolderType.DOCUMENT,
        description = "User Documents: Standard directory for user documents and text files.",
    )

    FileActionSheetContent(
        node = fileSys,
        onEnterClick = {},
        onRenameClick = {},
        onDeleteClick = {}
    )
}

/**
 * Preview for the detail row component.
 */
@Preview
@Composable
fun DetailRowPreview() {
    DetailRow("Size", "10 MB")
}
