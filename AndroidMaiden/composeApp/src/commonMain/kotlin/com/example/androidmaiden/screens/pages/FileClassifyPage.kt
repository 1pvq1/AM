package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.model.FileNode
import com.example.androidmaiden.utils.*
import com.example.androidmaiden.viewmodel.FileScannerViewModel
import com.example.androidmaiden.views.fileSys.ViewMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.ui.tooling.preview.Preview


data class FileCategory(
    val name: String,
    val icon: ImageVector,
    val type: String,
    val count: Int? = null,
    val totalSizeMb: Long? = null,
    val files: List<FileNode> = emptyList()
)

private val defaultCategories = listOf(
    FileCategory("Images", Icons.Default.Image, "Images"),
    FileCategory("Videos", Icons.Default.Videocam, "Videos"),
    FileCategory("Audio", Icons.Default.MusicNote, "Audio"),
    FileCategory("Documents", Icons.Default.Description, "Documents"),
    FileCategory("APKs", Icons.Default.Android, "APKs"),
    FileCategory("Archives", Icons.Default.Archive, "Archives"),
    FileCategory("Large Files (> 50MB)", Icons.Default.DiscFull, "LargeFiles"),
    FileCategory("Recent Files (Last 7 Days)", Icons.Default.Schedule, "RecentFiles"),
    FileCategory("Other", Icons.AutoMirrored.Filled.InsertDriveFile, "Other")
)

private suspend fun calculateCategoryDetails(
    root: FileNode,
    baseCategories: List<FileCategory>
): List<FileCategory> = withContext(Dispatchers.Default) {
    val files = root.flatten().filter { !it.isFolder }
    val counts = root.countByType()
    val sizes = root.totalSizeByType()

    val updated = baseCategories.map { category ->
        when (category.type) {
            "LargeFiles" -> {
                val largeFiles = root.getLargeFiles(thresholdMb = 50)
                val sizeMb = largeFiles.sumOf { it.size ?: 0L } / (1024 * 1024)
                category.copy(count = largeFiles.size, totalSizeMb = sizeMb, files = largeFiles)
            }

            "RecentFiles" -> {
                val recentFiles = root.getRecentFiles(withinMillis = 7 * 24 * 60 * 60 * 1000)
                val sizeMb = recentFiles.sumOf { it.size ?: 0L } / (1024 * 1024)
                category.copy(count = recentFiles.size, totalSizeMb = sizeMb, files = recentFiles)
            }

            else -> { // Standard file types including "Other"
                val filesForCategory = files.filter { it.extensionGroup() == category.type }
                val count = counts[category.type] ?: 0
                val sizeMb = (sizes[category.type] ?: 0L) / (1024 * 1024)
                category.copy(count = count, totalSizeMb = sizeMb, files = filesForCategory)
            }
        }
    }
    updated // No longer sorting to preserve section order
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyPage(onBack: () -> Unit = {}) {
    val vm = remember { FileScannerViewModel() }
    var categories by remember { mutableStateOf(defaultCategories) }
    var viewMode by remember { mutableStateOf(ViewMode.LIST) } // Default View state

    LaunchedEffect(Unit) { // Load real data
        vm.loadRoot(useMock = false)
    }

    val isLoading = vm.isLoading
    val loadError = vm.loadError
    val fileTree = vm.fileTree

    LaunchedEffect(fileTree) {
        if (fileTree != null) {
            categories = calculateCategoryDetails(fileTree, defaultCategories)
        }
    }

    var selectedCategory by remember { mutableStateOf<FileCategory?>(null) }

    if (selectedCategory != null) {
        FileListPage(
            categoryName = selectedCategory!!.name,
            files = selectedCategory!!.files,
            onBack = { selectedCategory = null }
        )
    } else {
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
                        // --- View Switcher Button ---
                        IconButton(onClick = {
                            viewMode =
                                if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                        }) {
                            Icon(
                                imageVector = if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.List,
                                contentDescription = "Switch View"
                            )
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
                if (fileTree != null) {
                    Text(
                        text = "Scanned path: ${fileTree.path ?: "Device Root"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    if (viewMode == ViewMode.LIST) {
                        CategoryListView(categories) { selectedCategory = it }
                    } else {
                        CategoryGridView(categories) { selectedCategory = it }
                    }

                    when {
                        isLoading && fileTree == null -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        loadError != null -> {
                            Text(
                                text = "Error: $loadError",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- List Layout ---
@Composable
private fun CategoryListView(categories: List<FileCategory>, onSelect: (FileCategory) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionHeader("Common Types") }
        items(categories.subList(0, 6)) { FileCategoryCard(it, onClick = { onSelect(it) }) }

        item { SectionHeader("Size and Date") }
        items(categories.subList(6, 8)) { FileCategoryCard(it, onClick = { onSelect(it) }) }

        item { SectionHeader("Other") }
        items(categories.subList(8, categories.size)) {
            FileCategoryCard(
                it,
                onClick = { onSelect(it) })
        }
    }
}

// --- Compact Grid Layout ---
@Composable
private fun CategoryGridView(categories: List<FileCategory>, onSelect: (FileCategory) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(2) }) { SectionHeader("Common Types") }
        items(categories.subList(0, 6)) { FileCategoryStrip(it, onClick = { onSelect(it) }) }

        item(span = { GridItemSpan(2) }) { SectionHeader("Size and Date") }
        items(categories.subList(6, 8)) { FileCategoryStrip(it, onClick = { onSelect(it) }) }

        item(span = { GridItemSpan(2) }) { SectionHeader("Other") }
        items(categories.subList(8, categories.size)) {
            FileCategoryStrip(
                it,
                onClick = { onSelect(it) })
        }
    }
}

// --- NEW: Small Strip Block (Compact) ---
@Composable
private fun FileCategoryStrip(category: FileCategory, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${category.count ?: 0} items",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider()
    }
}

@Composable
private fun FileCategoryCard(category: FileCategory, onClick: () -> Unit) {
    val description = if (category.count != null && category.totalSizeMb != null) {
        "${category.count} files • ${category.totalSizeMb} MB"
    } else {
        "Calculating..."
    }
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
                    text = description,
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
