package com.example.androidmaiden.screens.pages

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.model.*
import com.example.androidmaiden.utils.FileTypeUtils
import com.example.androidmaiden.viewModels.*
import com.example.androidmaiden.views.SectionHeader
import com.example.androidmaiden.views.fileSys.ViewMode
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyPage(onBack: () -> Unit = {}) {
    val vm: PersistentFileViewModel = koinViewModel()

    // 1. Observe state from the ViewModel (Survives rotation)
    val categories by vm.categories.collectAsState()
    val isSyncing by vm.isSyncing.collectAsState()
    val viewMode by vm.viewMode.collectAsState()
    val selectedCategory by vm.selectedCategory.collectAsState()

    // 2. Trigger incremental sync once on startup
    LaunchedEffect(Unit) {
        vm.startSync()
    }

    if (selectedCategory != null) {
        FilesListPage(
            categoryName = selectedCategory!!.name,
            files = selectedCategory!!.files,
            onBack = { vm.selectCategory(null) }
        )
    } else {
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
                        // --- Re-scan Button ---
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

                        IconButton(
                            onClick = { vm.startSync() },
                            enabled = !isSyncing
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Re-scan",
                                modifier = Modifier.rotate(rotation)
                            )
                        }

                        // --- View Switcher Button ---
                        IconButton(onClick = {
                            val nextMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                            vm.setViewMode(nextMode)
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
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
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
