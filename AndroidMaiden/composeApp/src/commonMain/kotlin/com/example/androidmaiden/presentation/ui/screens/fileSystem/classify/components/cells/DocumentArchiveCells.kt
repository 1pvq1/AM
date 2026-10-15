package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.cells

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.core.util.*
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.theme.core.LocalFileTypeColors

/**
 * Generic item cell composable for non-specialized file types in Grid or List view modes.
 *
 * @param file The [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun GenericItemCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    if (viewMode == ViewMode.LIST) {
        ListItem(
            headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            supportingContent = { Text("${formatSize(file.size)} • ${file.path}") },
            leadingContent = {
                Icon(
                    imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = modifier
        )
    } else {
        Card(modifier = modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.AutoMirrored.Filled.InsertDriveFile, null, modifier = Modifier.size(48.dp))
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

/**
 * Item cell composable for document files with extension badge chips.
 *
 * @param file The document [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun DocumentsCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    val fileTypeColors = LocalFileTypeColors.current
    val ext = file.name.substringAfterLast(".").uppercase()
    val subcategory = remember(file) { FileTypeUtils.getDocumentSubcategory(file) }

    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)} • ${subcategory.displayName}") },
        leadingContent = {
            Surface(
                color = when(ext) {
                    "PDF" -> fileTypeColors.pdf
                    "DOC", "DOCX" -> fileTypeColors.doc
                    "XLS", "XLSX" -> fileTypeColors.xls
                    "TXT", "MD" -> fileTypeColors.txt
                    else -> MaterialTheme.colorScheme.secondary
                },
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(ext, color = Color.White, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        },
        modifier = modifier
    )
}

/**
 * Item cell composable for archive files (ZIP, RAR, etc.).
 *
 * @param file The archive [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun ArchiveCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    val fileTypeColors = LocalFileTypeColors.current
    val subcategory = remember(file) { FileTypeUtils.getArchiveSubcategory(file) }

    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)} • ${subcategory.displayName}") },
        leadingContent = { Icon(Icons.Default.Archive, null, tint = fileTypeColors.archive) },
        modifier = modifier
    )
}
