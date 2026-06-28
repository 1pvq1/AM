package com.example.androidmaiden.presentation.ui.screens.fileSystem.organize.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.data.local.*
import com.example.androidmaiden.util.formatSize
import com.example.androidmaiden.util.formatDateTime

/**
 * View showing search results for files and allowing tags to be added.
 */
@Composable
fun TagSearchView(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<FileMetadata>,
    onAddTagToFile: (FileMetadata, Tag) -> Unit,
    availableTags: List<Tag>
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        if (searchResults.isEmpty() && query.length >= 2) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No files found", color = MaterialTheme.colorScheme.outline)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(searchResults, key = { it.path }) { file ->
                    FileTagCard(
                        file = file,
                        availableTags = availableTags,
                        onAddTagToFile = onAddTagToFile
                    )
                }
            }
        }
    }
}

/**
 * A card representing a file with options to add tags.
 */
@Composable
fun FileTagCard(
    file: FileMetadata,
    tags: List<Tag> = emptyList(),
    availableTags: List<Tag>,
    onAddTagToFile: (FileMetadata, Tag) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .let { if (onClick != null) it.clickable { onClick() } else it },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        file.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!file.isDirectory) {
                        Text(
                            "${formatSize(file.size)} | ${formatDateTime(file.lastModified)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            
            if (tags.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(tags) { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag.name, fontSize = 10.sp) },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            com.example.androidmaiden.presentation.ui.screens.fileSystem.organize.utils.ColorUtils.parseHexColor(tag.colorHex),
                                            androidx.compose.foundation.shape.CircleShape
                                        )
                                )
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            
            Text("Add Tag:", style = MaterialTheme.typography.labelSmall)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(availableTags.filter { it !in tags }) { tag ->
                    SuggestionChip(
                        onClick = { onAddTagToFile(file, tag) },
                        label = { Text(tag.name, fontSize = 10.sp) }
                    )
                }
            }
        }
    }
}
