package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.ui.icons.fileIcon
import com.example.androidmaiden.ui.icons.folderIcon
import com.example.androidmaiden.utils.formatSize
import com.example.androidmaiden.utils.formatDateTime

/**
 * Item component representing a single file or folder.
 * Enhanced to support single click for info and double click for navigation.
 *
 * @param node The file system node to display.
 * @param modifier The modifier to be applied to the component.
 * @param onClick Callback for single click interaction.
 * @param onDoubleClick Callback for double click interaction.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileItem(
    node: FileSysNode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {}
) {
    val icon = if (node.isFolder) {
        folderIcon(node.folderType)
    } else {
        fileIcon(node.name)
    }

    ListItem(
        headlineContent = {
            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
        },
        supportingContent = {
            Column {
                if (node.description.isNotBlank() && !node.description.startsWith("Path:")) {
                    Text(
                        node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1
                    )
                }
                val timeText = node.lastModified?.let { formatDateTime(it) }
                    ?: stringResource(id = "unknown_time")

                if (node.isFolder) {
                    val folderCount = node.children.count { it.isFolder }
                    val fileCount = node.children.count { !it.isFolder }
                    Text(
                        stringResource(id = "folder_details", folderCount, fileCount, timeText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    val sizeText = node.size?.let { " (${formatSize(it)})" } ?: ""
                    Text(
                        "$timeText$sizeText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = node.name,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
    )
}
