package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.model.FileNode
import com.example.androidmaiden.views.fileSys.ViewMode
import coil3.compose.AsyncImage // Assuming Coil is used for images/icons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileListPage(categoryName: String, files: List<FileNode>, onBack: () -> Unit) {
    // 1. Determine Initial View Mode based on Category Name/Type
    val initialMode = remember(categoryName) {
        when {
            categoryName.contains("Images", ignoreCase = true) -> ViewMode.GRID
            categoryName.contains("APKs", ignoreCase = true) -> ViewMode.GRID
            else -> ViewMode.LIST
        }
    }

    var viewMode by remember { mutableStateOf(initialMode) }
    var sortOrder by remember { mutableStateOf(SortOrder.NAME) }
    var showSortMenu by remember { mutableStateOf(false) }

    // 2. Sort the files based on selection
    val sortedFiles = remember(files, sortOrder) {
        when (sortOrder) {
            SortOrder.NAME -> files.sortedBy { it.name }
            SortOrder.SIZE -> files.sortedByDescending { it.size ?: 0L }
            SortOrder.DATE -> files.sortedByDescending { it.lastModified }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Sorting Button
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Sort by Name") },
                                onClick = { sortOrder = SortOrder.NAME; showSortMenu = false },
                                leadingIcon = { Icon(Icons.Default.SortByAlpha, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Size") },
                                onClick = { sortOrder = SortOrder.SIZE; showSortMenu = false },
                                leadingIcon = { Icon(Icons.Default.Straighten, null) }
                            )
                        }
                    }
                    // View Switcher Button
                    IconButton(onClick = {
                        viewMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                    }) {
                        Icon(
                            imageVector = if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = "Switch View"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (viewMode == ViewMode.LIST) {
                FileListView(sortedFiles)
            } else {
                FileGridView(sortedFiles, isImageCategory = categoryName.contains("Images"))
            }
        }
    }
}

@Composable
private fun FileListView(files: List<FileNode>) {
    LazyColumn {
        items(files) { file ->
            ListItem(
                headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                supportingContent = { Text("${(file.size ?: 0L) / 1024} KB • ${file.path ?: ""}") },
                leadingContent = {
                    Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun FileGridView(files: List<FileNode>, isImageCategory: Boolean) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = if (isImageCategory) 120.dp else 100.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files) { file ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                    if (isImageCategory) {
                        // Image Thumbnail Placeholder
                        AsyncImage(
                            model = file.path,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // App/APK Icon Placeholder
                        Icon(
                            imageVector = if (file.name.endsWith(".apk")) Icons.Default.Android else Icons.Default.InsertDriveFile,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp).padding(bottom = 8.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

enum class SortOrder { NAME, SIZE, DATE }