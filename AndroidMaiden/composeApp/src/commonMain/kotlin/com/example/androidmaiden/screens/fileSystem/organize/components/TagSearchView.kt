package com.example.androidmaiden.screens.fileSystem.organize.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.Tag

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
                    FileTagCard(file, availableTags, onAddTagToFile)
                }
            }
        }
    }
}

@Composable
fun FileTagCard(
    file: FileMetadata,
    availableTags: List<Tag>,
    onAddTagToFile: (FileMetadata, Tag) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                Text(file.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text(file.path, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            
            Spacer(Modifier.height(8.dp))
            
            Text("Add Tag:", style = MaterialTheme.typography.labelSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                items(availableTags) { tag ->
                    SuggestionChip(
                        onClick = { onAddTagToFile(file, tag) },
                        label = { Text(tag.name, fontSize = 10.sp) }
                    )
                }
            }
        }
    }
}
