package com.example.androidmaiden.screens.fileSystem.organize.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.utils.formatSize

@Composable
fun TaggedFilesView(
    tagName: String,
    files: List<FileMetadata>,
    onRemoveTag: (FileMetadata) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Files tagged with \"$tagName\"", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        
        if (files.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No files with this tag", color = MaterialTheme.colorScheme.outline)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(files, key = { it.path }) { file ->
                    ListItem(
                        headlineContent = { Text(file.name) },
                        supportingContent = { Text(formatSize(file.size)) },
                        leadingContent = {
                            Icon(
                                if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile, 
                                null
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { onRemoveTag(file) }) {
                                Icon(
                                    Icons.Default.RemoveCircleOutline, 
                                    "Remove Tag", 
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
