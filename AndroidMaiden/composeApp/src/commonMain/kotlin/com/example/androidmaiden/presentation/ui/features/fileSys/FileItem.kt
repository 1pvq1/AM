package com.example.androidmaiden.presentation.ui.features.fileSys

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.domain.model.FileItemDescriptionMode
import com.example.androidmaiden.domain.model.FileItemIconStyle
import com.example.androidmaiden.domain.model.FileSysNode
import com.example.androidmaiden.domain.model.FolderType
import com.example.androidmaiden.domain.model.NodeType
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.icons.fileIcon
import com.example.androidmaiden.presentation.ui.icons.folderIcon
import com.example.androidmaiden.core.util.formatSize
import com.example.androidmaiden.core.util.formatDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Item component representing a single file or folder node in list or grid views.
 * Supports customizable description modes (Concise summary, Full text, or Hidden), icon styling, and detail toggles.
 *
 * @param node The file system node to render.
 * @param modifier The modifier to be applied to the component.
 * @param onClick Callback for single tap interaction.
 * @param onDoubleClick Callback for double tap interaction (e.g. folder entry).
 * @param descriptionMode Display mode for architectural descriptions (CONCISE, FULL, HIDDEN).
 * @param iconStyle Icon theme style (DEFAULT, OUTLINED, MINIMAL).
 * @param showDetails Whether timestamp and size/count metadata details are displayed.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileItem(
    node: FileSysNode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    descriptionMode: FileItemDescriptionMode = FileItemDescriptionMode.CONCISE,
    iconStyle: FileItemIconStyle = FileItemIconStyle.DEFAULT,
    showDetails: Boolean = true
) {
    val baseIcon = if (node.isFolder) {
        folderIcon(node.folderType)
    } else {
        fileIcon(node.name)
    }

    val iconTint = when (iconStyle) {
        FileItemIconStyle.DEFAULT -> MaterialTheme.colorScheme.primary
        FileItemIconStyle.OUTLINED -> MaterialTheme.colorScheme.outline
        FileItemIconStyle.MINIMAL -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val iconSize = when (iconStyle) {
        FileItemIconStyle.MINIMAL -> 18.dp
        else -> 24.dp
    }

    ListItem(
        headlineContent = {
            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                // Render architectural description according to descriptionMode
                if (descriptionMode != FileItemDescriptionMode.HIDDEN &&
                    node.description.isNotBlank() &&
                    !node.description.startsWith("Path:")
                ) {
                    val maxLines = if (descriptionMode == FileItemDescriptionMode.CONCISE) 1 else Int.MAX_VALUE
                    Text(
                        text = node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = maxLines,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Render metadata timestamp and size/count if showDetails is true
                if (showDetails) {
                    val timeText = node.lastModified?.let { formatDateTime(it) }
                        ?: stringResource(id = "unknown_time")

                    if (node.isFolder) {
                        val folderCount = node.children.count { it.isFolder }
                        val fileCount = node.children.count { !it.isFolder }
                        Text(
                            text = stringResource(id = "folder_details", folderCount, fileCount, timeText),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        val sizeText = node.size?.let { " (${formatSize(it)})" } ?: ""
                        Text(
                            text = "$timeText$sizeText",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        },
        leadingContent = {
            Icon(
                imageVector = baseIcon,
                contentDescription = node.name,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
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

/**
 * Preview for the file item component.
 */
@Preview
@Composable
fun FileItemPreview() {
    AppTheme {
        Surface {
            Column {
                FileItem(
                    node = FileSysNode(
                        name = "Documents",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.DOCUMENT,
                        description = "User Documents: Standard directory for user documents and text files.",
                        children = listOf(
                            FileSysNode(name = "Resume.pdf", nodeType = NodeType.FILE),
                            FileSysNode(name = "Images", nodeType = NodeType.FOLDER)
                        ),
                        lastModified = 1715856000000L
                    ),
                    descriptionMode = FileItemDescriptionMode.CONCISE,
                    iconStyle = FileItemIconStyle.DEFAULT,
                    showDetails = true
                )
                FileItem(
                    node = FileSysNode(
                        name = "Beach_Sunset.jpg",
                        nodeType = NodeType.FILE,
                        size = 3500000L,
                        lastModified = 1715856000000L
                    ),
                    descriptionMode = FileItemDescriptionMode.FULL,
                    iconStyle = FileItemIconStyle.OUTLINED,
                    showDetails = true
                )
            }
        }
    }
}
