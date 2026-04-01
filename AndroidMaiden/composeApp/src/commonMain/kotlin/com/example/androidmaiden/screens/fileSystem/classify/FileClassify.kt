package com.example.androidmaiden.screens.fileSystem.classify

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.model.*
import com.example.androidmaiden.views.fileSys.ViewMode
import com.example.androidmaiden.utils.*
import com.example.androidmaiden.viewModels.*
import com.example.androidmaiden.views.SectionHeader
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyPage(onBack: () -> Unit = {}) {
    val vm: PersistentFileViewModel = koinViewModel()

    // 1. Observe state from the ViewModel
    val categories by vm.categories.collectAsState()
    val isSyncing by vm.isSyncing.collectAsState()
    val viewMode by vm.viewMode.collectAsState()
    val selectedCategory by vm.selectedCategory.collectAsState()
    val stats by vm.storageStats.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val searchResults by vm.searchResults.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }
    var previewFile by remember { mutableStateOf<FileMetadata?>(null) }

    // 2. Trigger incremental sync once on startup
    LaunchedEffect(Unit) {
        vm.startSync()
    }

    if (selectedCategory != null) {
        FilesListPage(
            categoryName = selectedCategory!!.name,
            files = selectedCategory!!.files,
            onBack = { vm.selectCategory(null) },
            onDelete = { vm.deleteFile(it.path) }
        )
    } else {
        Scaffold(
            topBar = {
                if (isSearchActive) {
                    SearchTopBar(
                        query = searchQuery,
                        onQueryChange = { vm.updateSearchQuery(it) },
                        onClose = { 
                            isSearchActive = false
                            vm.updateSearchQuery("")
                        }
                    )
                } else {
                    MainTopBar(
                        isSyncing = isSyncing,
                        viewMode = viewMode,
                        onBack = onBack,
                        onSync = { vm.startSync() },
                        onToggleView = {
                            val nextMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                            vm.setViewMode(nextMode)
                        },
                        onSearchOpen = { isSearchActive = true }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                if (isSearchActive && searchQuery.length >= 2) {
                    SearchResultsView(searchResults) { file ->
                        previewFile = file
                    }
                } else {
                    Column {
                        StorageSummaryHeader(stats)
                        
                        Box(modifier = Modifier.weight(1f)) {
                            if (viewMode == ViewMode.LIST) {
                                CategoryListView(categories) { vm.selectCategory(it) }
                            } else {
                                CategoryGridView(categories) { vm.selectCategory(it) }
                            }

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
    }

    // Global Preview Overlay (for search results)
    previewFile?.let { file ->
        FilePreviewOverlay(
            file = file,
            onDismiss = { previewFile = null }
        )
    }
}

@Composable
private fun StorageSummaryHeader(stats: StorageStats) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Storage,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = "Total Storage Used",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = formatSize(stats.totalSize),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${stats.fileCount} Files • ${stats.folderCount} Folders",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    isSyncing: Boolean,
    viewMode: ViewMode,
    onBack: () -> Unit,
    onSync: () -> Unit,
    onToggleView: () -> Unit,
    onSearchOpen: () -> Unit
) {
    TopAppBar(
        title = { Text("Classify Files") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onSearchOpen) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            
            val rotation by animateFloatAsState(
                targetValue = if (isSyncing) 360f else 0f,
                animationSpec = if (isSyncing) {
                    infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                } else {
                    tween(0)
                }
            )

            IconButton(onClick = onSync, enabled = !isSyncing) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Re-scan",
                    modifier = Modifier.rotate(rotation)
                )
            }

            IconButton(onClick = onToggleView) {
                Icon(
                    imageVector = if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.List,
                    contentDescription = "Switch View"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search files...") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close Search")
            }
        },
        actions = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear text")
                }
            }
        }
    )
}

@Composable
private fun SearchResultsView(results: List<FileMetadata>, onFileClick: (FileMetadata) -> Unit) {
    if (results.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No results found", color = MaterialTheme.colorScheme.outline)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(results) { file ->
                ListItem(
                    headlineContent = { Text(file.name) },
                    supportingContent = { Text(file.path, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    leadingContent = {
                        Icon(
                            imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth().clickable { onFileClick(file) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

// --- List Layout ---
@Composable
private fun CategoryListView(categories: List<FileCategory>, onSelect: (FileCategory) -> Unit) {
    val commonTypes = remember(categories) {
        val commonNames = FileTypeUtils.categoryDefinitions.map { it.name }
        categories.filter { it.name in commonNames }
    }
    val analysisTypes = remember(categories) {
        val analysisNames = FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }.map { it.name }
        categories.filter { it.name in analysisNames }
    }
    val otherTypes = remember(categories) {
        val knownNames = (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }).map { it.name }
        categories.filter { it.name !in knownNames }
    }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (commonTypes.isNotEmpty()) {
            item { SectionHeader("Common Types") }
            items(commonTypes) { FileCategoryCard(it, onClick = { onSelect(it) }) }
        }

        if (analysisTypes.isNotEmpty()) {
            item { SectionHeader("Size and Date") }
            items(analysisTypes) { FileCategoryCard(it, onClick = { onSelect(it) }) }
        }

        if (otherTypes.isNotEmpty()) {
            item { SectionHeader("Other") }
            items(otherTypes) {
                FileCategoryCard(
                    it,
                    onClick = { onSelect(it) })
            }
        }
    }
}

// --- Compact Grid Layout ---
@Composable
private fun CategoryGridView(categories: List<FileCategory>, onSelect: (FileCategory) -> Unit) {
    val commonTypes = remember(categories) {
        val commonNames = FileTypeUtils.categoryDefinitions.map { it.name }
        categories.filter { it.name in commonNames }
    }
    val analysisTypes = remember(categories) {
        val analysisNames = FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }.map { it.name }
        categories.filter { it.name in analysisNames }
    }
    val otherTypes = remember(categories) {
        val knownNames = (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }).map { it.name }
        categories.filter { it.name !in knownNames }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (commonTypes.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) { SectionHeader("Common Types") }
            items(commonTypes) { FileCategoryStrip(it, onClick = { onSelect(it) }) }
        }

        if (analysisTypes.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) { SectionHeader("Size and Date") }
            items(analysisTypes) { FileCategoryStrip(it, onClick = { onSelect(it) }) }
        }

        if (otherTypes.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) { SectionHeader("Other") }
            items(otherTypes) {
                FileCategoryStrip(
                    it,
                    onClick = { onSelect(it) })
            }
        }
    }
}

// --- Small Strip Block (Compact) ---
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
