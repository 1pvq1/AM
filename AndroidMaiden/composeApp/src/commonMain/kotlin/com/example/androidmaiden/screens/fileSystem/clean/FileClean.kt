package com.example.androidmaiden.screens.fileSystem.clean

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.TrashEntry
import com.example.androidmaiden.model.CleanupResult
import com.example.androidmaiden.model.CleanupStats
import com.example.androidmaiden.utils.formatSize
import com.example.androidmaiden.viewModels.FileClearViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileCleanPage(onBack: () -> Unit) {
    val vm: FileClearViewModel = koinViewModel()
    val stats by vm.cleanupStats.collectAsState()
    val result by vm.cleanupResult.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(result) {
        result?.let {
            when (it) {
                is CleanupResult.Success -> {
                    snackbarHostState.showSnackbar("Cleanup successful")
                    vm.resetResult()
                }
                is CleanupResult.Error -> {
                    snackbarHostState.showSnackbar("Error: ${it.message}")
                    vm.resetResult()
                }
                is CleanupResult.Loading -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("File Cleanup") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CleanupSummaryCard(stats)
            }

            item {
                Text("Redundant Files", style = MaterialTheme.typography.titleMedium)
            }

            item {
                CleanupActionCard(
                    title = "Duplicate Files",
                    subtitle = "${stats.duplicateFiles.size} files found",
                    icon = Icons.Default.CopyAll,
                    onClick = { vm.clearDuplicates() }
                )
            }

            item {
                CleanupActionCard(
                    title = "Empty Folders",
                    subtitle = "${stats.emptyFolders.size} folders found",
                    icon = Icons.Default.FolderOff,
                    onClick = { vm.clearEmptyFolders() }
                )
            }

            item {
                Text("Recycle Bin", style = MaterialTheme.typography.titleMedium)
            }

            if (stats.trashEntries.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Recycle bin is empty", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(stats.trashEntries) { entry ->
                    TrashItem(
                        entry = entry,
                        onRestore = { vm.restoreFromTrash(entry.trashPath) },
                        onDelete = { vm.deletePermanently(entry.trashPath) }
                    )
                }
            }
        }
    }
}

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

@Composable
fun TrashItem(entry: TrashEntry, onRestore: () -> Unit, onDelete: () -> Unit) {
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

@Preview
@Composable
fun PreviewFileCleanPage() {
    FileCleanPage {}
}