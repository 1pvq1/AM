package com.example.androidmaiden.screens.fileSystem.analyze

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.model.*
import com.example.androidmaiden.screens.pages.BasePage
import com.example.androidmaiden.ui.icons.fileIcon
import com.example.androidmaiden.ui.icons.folderIcon
import com.example.androidmaiden.utils.*
import com.example.androidmaiden.viewModels.FileScannerViewModel
import com.example.androidmaiden.viewModels.FolderAnalysisStats
import com.example.androidmaiden.views.fileSys.FileGridView
import com.example.androidmaiden.views.fileSys.FileListView
import com.example.androidmaiden.views.fileSys.FileTreeView
import com.example.androidmaiden.views.fileSys.ViewMode
import com.example.androidmaiden.views.panel.FileAnalysisToolbar
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

/**
 * Main screen for file system analysis.
 * Provides multiple view modes (List, Grid, Tree) and supports both mock and real data.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class, ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun FileAnalysisScreen(onNavigateUp: () -> Unit = {}) {

    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASC) }

    val vm: FileScannerViewModel = koinViewModel()
    val useMock = vm.useMock
    
    val root = vm.currentDirectory
    val stats = vm.folderStats
    val isLoading by vm.isLoading.collectAsState()
    val loadError by vm.error.collectAsState()
    val pathStack = vm.pathStack

    var selectedNode by remember { mutableStateOf<FileSysNode?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showStatsPopUp by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BasePage(
        title = stringResource(id = "file_analysis"),
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = {
            if (!vm.navigateBack()) {
                onNavigateUp()
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { showStatsPopUp = true }) {
                Icon(Icons.Default.BarChart, contentDescription = "Folder Stats")
            }
            FileAnalysisToolbar(
                viewMode = viewMode,
                onViewModeChange = { viewMode = it },
                useMock = useMock,
                onUseMockChange = { vm.toggleSource() },
                isAndroid = true, 
                sortMode = sortMode,
                onSortModeChange = { sortMode = it },
                sortOrder = sortOrder,
                onSortOrderChange = { sortOrder = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            // Interactive Breadcrumbs
            PathBreadcrumbs(
                pathStack = pathStack,
                onIndexClick = { vm.navigateToStackIndex(it) },
                onRootClick = { vm.navigateToStackIndex(-1) }
            )

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                loadError != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = loadError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                root != null -> {
                    val sortedRoot = root.copy(
                        children = root.sortedChildren(mode = sortMode, order = sortOrder)
                    )

                    // Unified Interaction Logic:
                    // Single Click -> Select (Show Info Sheet)
                    // Double Click -> Navigate (If folder)
                    val onNodeClick: (FileSysNode) -> Unit = { node ->
                        selectedNode = node
                    }
                    
                    val onNodeDoubleClick: (FileSysNode) -> Unit = { node ->
                        if (node.isFolder) {
                            vm.navigateTo(node)
                        } else {
                            selectedNode = node
                        }
                    }

                    Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                        when (viewMode) {
                            ViewMode.LIST -> FileListView(sortedRoot, onNodeClick, onNodeDoubleClick)
                            ViewMode.GRID -> FileGridView(sortedRoot, onNodeClick, onNodeDoubleClick)
                            ViewMode.TREE -> FileTreeView(sortedRoot, onNodeClick, onNodeDoubleClick)
                        }
                    }
                }
            }
        }
    }

    // --- Action Overlays ---

    // 1. Statistics Pop-up
    if (showStatsPopUp) {
        StatsPopUp(stats = stats, onDismiss = { showStatsPopUp = false })
    }

    // 2. Action Sheet / Analysis Info
    selectedNode?.let { node ->
        ModalBottomSheet(onDismissRequest = { selectedNode = null }) {
            FileActionSheetContent(
                node = node,
                onEnterClick = { 
                    if (node.isFolder) vm.navigateTo(node)
                    selectedNode = null
                },
                onRenameClick = { showRenameDialog = true },
                onDeleteClick = { showDeleteDialog = true }
            )
        }
    }

    // 3. Delete Confirmation
    if (showDeleteDialog && selectedNode != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete '${selectedNode?.name}'? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedNode?.let { vm.deleteNode(it) }
                        showDeleteDialog = false
                        selectedNode = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    // 4. Rename Input
    if (showRenameDialog && selectedNode != null) {
        var newName by remember { mutableStateOf(selectedNode?.name ?: "") }
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Item") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedNode?.let { vm.renameNode(it, newName) }
                        showRenameDialog = false
                        selectedNode = null
                    }
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) { Text("Cancel") }
            }
        )
    }
}

/**
 * Visual Pop-up for folder statistics.
 */
@Composable
fun StatsPopUp(stats: FolderAnalysisStats, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Folder Analysis",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Files", value = stats.fileCount.toString())
                    StatItem(label = "Folders", value = stats.folderCount.toString())
                    StatItem(label = "Size", value = formatSize(stats.totalSize))
                }

                Spacer(Modifier.height(24.dp))

                if (stats.totalSize > 0) {
                    DistributionBar(distribution = stats.typeDistribution, totalSize = stats.totalSize)
                } else {
                    Text("No data to analyze", color = MaterialTheme.colorScheme.outline)
                }

                Spacer(Modifier.height(24.dp))

                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun DistributionBar(distribution: Map<String, Long>, totalSize: Long) {
    val categoryColors = remember {
        mapOf(
            "Images" to Color(0xFF4CAF50),
            "Videos" to Color(0xFF2196F3),
            "Audio" to Color(0xFFE91E63),
            "Documents" to Color(0xFFFF9800),
            "APKs" to Color(0xFF3DDC84),
            "Archives" to Color(0xFF9C27B0),
            "Other" to Color(0xFF9E9E9E)
        )
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            distribution.forEach { (type, size) ->
                val weight = size.toFloat() / totalSize
                if (weight > 0.01f) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(weight)
                            .background(categoryColors[type] ?: categoryColors["Other"]!!)
                    )
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Detailed Legend
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            distribution.filter { it.value > 0 }.forEach { (type, size) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(categoryColors[type] ?: categoryColors["Other"]!!)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "$type: ${formatSize(size)} (${(size * 100 / totalSize)}%)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * Bottom sheet content for file/folder actions and analysis info.
 */
@Composable
fun FileActionSheetContent(
    node: FileSysNode,
    onEnterClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        ListItem(
            headlineContent = { Text(node.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            supportingContent = { Text(node.path ?: "N/A", style = MaterialTheme.typography.bodySmall) },
            leadingContent = {
                Icon(
                    imageVector = if (node.isFolder) folderIcon(node.folderType) else fileIcon(node.name),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        HorizontalDivider()
        
        if (node.isFolder) {
            ListItem(
                headlineContent = { Text("Open Folder") },
                leadingContent = { Icon(Icons.Default.FolderOpen, null) },
                modifier = Modifier.clickable { onEnterClick() }
            )
        }

        ListItem(
            headlineContent = { Text("Rename") },
            leadingContent = { Icon(Icons.Default.Edit, null) },
            modifier = Modifier.clickable { onRenameClick() }
        )
        ListItem(
            headlineContent = { Text("Delete", color = MaterialTheme.colorScheme.error) },
            leadingContent = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
            modifier = Modifier.clickable { onDeleteClick() }
        )
        
        HorizontalDivider()
        
        // Analysis Info section
        Text(
            text = "Item Details",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        DetailRow("Size", node.size?.let { formatSize(it) } ?: "N/A")
        DetailRow("Type", if (node.isFolder) "Folder" else "File")
        DetailRow("Last Modified", node.lastModified?.let { formatDateTime(it) } ?: "Unknown")
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * Clickable breadcrumbs for hierarchical navigation.
 */
@Composable
fun PathBreadcrumbs(
    pathStack: List<String>,
    onIndexClick: (Int) -> Unit,
    onRootClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    LaunchedEffect(pathStack.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Root",
            modifier = Modifier.clickable { onRootClick() },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (pathStack.isEmpty()) FontWeight.Bold else FontWeight.Normal
        )
        
        pathStack.forEachIndexed { index, path ->
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp).padding(horizontal = 4.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            
            val name = path.substringAfterLast("/", "...")
            val isLast = index == pathStack.size - 1
            
            Text(
                text = name,
                modifier = Modifier.clickable { onIndexClick(index) },
                color = if (isLast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * Item component representing a single file or folder.
 * Enhanced to support single click for info and double click for navigation.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileItem(
    node: FileSysNode, 
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {}
) {
    val icon = if (node.isFolder) {
        folderIcon(node.folderType)
    } else {
        fileIcon(node.name)
    }

    ListItem(
        headlineContent = { 
            Text(
                text = node.name, 
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            ) 
        },
        supportingContent = {
            Column {
                if (node.description.isNotBlank() && !node.description.startsWith("Path:")) {
                    Text(
                        node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1
                    )
                }
                val timeText = node.lastModified?.let { formatDateTime(it) }
                    ?: stringResource(id = "unknown_time")
                
                if (node.isFolder) {
                    val folderCount = node.children.count { it.isFolder }
                    val fileCount = node.children.count { !it.isFolder }
                    Text(
                        stringResource(id = "folder_details", folderCount, fileCount, timeText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    val sizeText = node.size?.let { " (${formatSize(it)})" } ?: ""
                    Text(
                        "$timeText$sizeText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = node.name,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
    )
}
