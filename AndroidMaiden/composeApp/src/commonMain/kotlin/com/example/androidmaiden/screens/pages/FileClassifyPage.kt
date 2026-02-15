package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.model.FileNode
import com.example.androidmaiden.utils.*
import com.example.androidmaiden.viewmodel.FileScannerViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

data class FileCategory(
    val name: String,
    val icon: ImageVector,
    val count: Int,
    val totalSizeMb: Int,
    val files: List<FileNode> = emptyList()
)

fun generateFileCategories(root: FileNode): List<FileCategory> {
    val files = root.flatten().filter { !it.isFolder }
    val counts = root.countByType()
    val sizes = root.totalSizeByType()

    val categories = counts.map { (type, count) ->
        val sizeMb = (sizes[type] ?: 0L) / (1024 * 1024)
        val icon = when (type) {
            "Images" -> Icons.Default.Image
            "Videos" -> Icons.Default.Videocam
            "Audio" -> Icons.Default.MusicNote
            "Documents" -> Icons.Default.Description
            "APKs" -> Icons.Default.Android
            "Archives" -> Icons.Default.Archive
            else -> Icons.AutoMirrored.Filled.InsertDriveFile
        }
        val filesForCategory = files.filter { it.extensionGroup() == type }
        FileCategory(type, icon, count, sizeMb.toInt(), filesForCategory)
    }.toMutableList()

    val largeFiles = root.getLargeFiles(thresholdMb = 50)
    if (largeFiles.isNotEmpty()) {
        val largeFilesSizeMb = largeFiles.sumOf { it.size ?: 0L } / (1024 * 1024)
        categories.add(
            FileCategory(
                name = "Large Files ( > 50MB)",
                icon = Icons.Default.DiscFull,
                count = largeFiles.size,
                totalSizeMb = largeFilesSizeMb.toInt(),
                files = largeFiles
            )
        )
    }

    val recentFiles = root.getRecentFiles(withinMillis = 7 * 24 * 60 * 60 * 1000) // 7 days
    if (recentFiles.isNotEmpty()) {
        val recentFilesSizeMb = recentFiles.sumOf { it.size ?: 0L } / (1024 * 1024)
        categories.add(
            FileCategory(
                name = "Recent Files (Last 7 Days)",
                icon = Icons.Default.Schedule,
                count = recentFiles.size,
                totalSizeMb = recentFilesSizeMb.toInt(),
                files = recentFiles
            )
        )
    }

    return categories.sortedByDescending { it.totalSizeMb }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyPage(onBack: () -> Unit = {}) {
    val vm = remember { FileScannerViewModel() }
    LaunchedEffect(vm.useMock) { // Reload when data source changes
        vm.loadRoot()
    }

    val isLoading = vm.isLoading
    val loadError = vm.loadError
    val fileTree = vm.fileTree
    val useMock = vm.useMock

    var selectedCategory by remember { mutableStateOf<FileCategory?>(null) }
    var showItemsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Classify Files") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Mock", style = MaterialTheme.typography.bodySmall)
                        Switch(checked = useMock, onCheckedChange = { vm.toggleSource() })
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                if (useMock) "Current data type: Simulation"
                else "Current data type: Real",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    loadError != null -> {
                        Text(
                            text = "Error: $loadError",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    fileTree != null -> {
                        val categories = remember(fileTree) { generateFileCategories(fileTree) }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(categories) { category ->
                                FileCategoryCard(
                                    category,
                                    onClick = {
                                        selectedCategory = category
                                        showItemsDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showItemsDialog && selectedCategory != null) {
        val cat = selectedCategory!!
        AlertDialog(
            onDismissRequest = { showItemsDialog = false },
            confirmButton = {
                TextButton(onClick = { showItemsDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text(cat.name) },
            text = {
                if (cat.files.isEmpty()) {
                    Text("No files found.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 360.dp)
                    ) {
                        items(cat.files) { file ->
                            ListItem(
                                headlineContent = { Text(file.name) },
                                supportingContent = { Text("${(file.size ?: 0L) / 1024} KB") }
                            )
                            Divider()
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun FileCategoryCard(category: FileCategory, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(category.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${category.count} files • ${category.totalSizeMb} MB",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileClassifyPagePreview() {
    FileClassifyPage()
}
