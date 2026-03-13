package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.model.*
import com.example.androidmaiden.viewmodel.*
import com.example.androidmaiden.views.SectionHeader
import com.example.androidmaiden.views.fileSys.ViewMode
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


/* val initialCategories = listOf(
    FileCategory("Images", Icons.Default.Image, "Images"),
    FileCategory("Videos", Icons.Default.Videocam, "Videos"),
    FileCategory("Audio", Icons.Default.MusicNote, "Audio"),
    FileCategory("Documents", Icons.Default.Description, "Documents"),
    FileCategory("APKs", Icons.Default.Android, "APKs"),
    FileCategory("Archives", Icons.Default.Archive, "Archives"),
    FileCategory("Large Files (> 50MB)", Icons.Default.DiscFull, "LargeFiles"),
    FileCategory("Recent Files (Last 7 Days)", Icons.Default.Schedule, "RecentFiles"),
    FileCategory("Other", Icons.AutoMirrored.Filled.InsertDriveFile, "Other")
)*/

// Instead of a hardcoded list, use the central definitions for the initial UI state, see in vm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyPage(onBack: () -> Unit = {}) {
//    var categories by remember { mutableStateOf(initialCategories) }
//    var viewMode by remember { mutableStateOf(ViewMode.LIST) } // Default View state
//    var selectedCategory by remember { mutableStateOf<FileCategory?>(null) }
//    val vm = remember { FileScannerViewModel() }

//    val isLoading = vm.isLoading
//    val loadError = vm.loadError
//    val fileTree = vm.fileTree

//    LaunchedEffect(fileTree) {
//        if (fileTree != null) {
//            categories = calculateCategoryDetails(fileTree, initialCategories)
//        }
//    }

    val vm: PersistentFileViewModel = koinViewModel()

    // 1. Observe pre-calculated categories from the VM
    val categories by vm.categories.collectAsState()
    val isSyncing by vm.isSyncing.collectAsState()

    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var selectedCategory by remember { mutableStateOf<FileCategory?>(null) }

    // 2. Trigger incremental sync once on startup
    LaunchedEffect(Unit) { // Load real data
//        vm.loadRoot(useMock = false)
        vm.startSync()
    }

    if (selectedCategory != null) {
        FilesListPage(
            categoryName = selectedCategory!!.name,
            files = selectedCategory!!.files,
            onBack = { selectedCategory = null }
        )
    } else {
        val scannedPath = vm.currentScannedPath.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Classify Files") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
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
            Column(modifier = Modifier.fillMaxSize()) {
//                Text(
//                    text = "Scanned path: $scannedPath",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.outline,
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                )
                Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                    // 3. Render the categories directly
                    if (viewMode == ViewMode.LIST) {
                        CategoryListView(categories) { selectedCategory = it }
                    } else {
                        CategoryGridView(categories) { selectedCategory = it }
                    }

                    // 4. Show loading indicator only during the initial scan or background sync
                    if (isSyncing && categories.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// --- List Layout ---
@Composable
private fun CategoryListView(categories: List<FileCategory>, onSelect: (FileCategory) -> Unit) {
    val commonType = categories.subList(0, 6)
    val sizeAndDateType = categories.subList(6, 8)
    val otherType = categories.subList(8, categories.size)
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionHeader("Common Types") }
        items(commonType) { FileCategoryCard(it, onClick = { onSelect(it) }) }

        item { SectionHeader("Size and Date") }
        items(sizeAndDateType) { FileCategoryCard(it, onClick = { onSelect(it) }) }

        item { SectionHeader("Other") }
        items(otherType) {
            FileCategoryCard(
                it,
                onClick = { onSelect(it) })
        }
    }
}

// --- Compact Grid Layout ---
@Composable
private fun CategoryGridView(categories: List<FileCategory>, onSelect: (FileCategory) -> Unit) {
    val commonType = categories.subList(0, 6)
    val sizeAndDateType = categories.subList(6, 8)
    val otherType = categories.subList(8, categories.size)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(2) }) { SectionHeader("Common Types") }
        items(commonType) { FileCategoryStrip(it, onClick = { onSelect(it) }) }

        item(span = { GridItemSpan(2) }) { SectionHeader("Size and Date") }
        items(sizeAndDateType) { FileCategoryStrip(it, onClick = { onSelect(it) }) }

        item(span = { GridItemSpan(2) }) { SectionHeader("Other") }
        items(otherType) {
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

@Preview
@Composable
fun CategoryListViewPreview() {
    CategoryListView(initialCategories) {}
}

@Preview
@Composable
fun CategoryGridViewPreview() {
    CategoryGridView(initialCategories) {}
}

