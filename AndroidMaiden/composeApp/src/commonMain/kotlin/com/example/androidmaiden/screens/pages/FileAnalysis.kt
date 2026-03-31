package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.model.*
import com.example.androidmaiden.ui.icons.fileIcon
import com.example.androidmaiden.ui.icons.folderIcon
import com.example.androidmaiden.utils.SortMode
import com.example.androidmaiden.utils.SortOrder
import com.example.androidmaiden.utils.formatDateTime
import com.example.androidmaiden.utils.sortedChildren
import com.example.androidmaiden.viewmodel.FileScannerViewModel
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.views.panel.FileAnalysisToolbar
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun FileAnalysisScreen(onNavigateUp: () -> Unit = {}) {

    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASC) }
//    var useMock by remember { mutableStateOf(true) }

    val vm = remember { FileScannerViewModel() }
    val useMock = vm.useMock
    LaunchedEffect(Unit) { vm.loadRoot(useMock) }
    val root = vm.fileTree
    // Use collectAsState for StateFlow from BaseViewModel
    val isLoading by vm.isLoading.collectAsState()
    val loadError by vm.error.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BasePage(
        title = stringResource(id = "file_analysis"),
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = onNavigateUp,
        scrollBehavior = scrollBehavior,
        actions = {
            FileAnalysisToolbar(
                viewMode = viewMode,
                onViewModeChange = { viewMode = it },
                useMock = useMock,
//                onUseMockChange = { useMock = it },
                onUseMockChange = {  vm.toggleSource() },
                isAndroid = true, // TODO: Replace with expect/actual or Build check
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
                .padding(8.dp)
        ) {
            Text(
                if (useMock) stringResource(id = "current_data_type_simulation")
                else stringResource(id = "current_data_type_real") + "\nScanned path: ${root?.path ?: "Device Root"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                loadError != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = loadError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                root != null -> {
                    // Sort files based on sortMode
                    val sortedRoot = root.copy(
                        children = root.sortedChildren(mode = sortMode, order = sortOrder)
                    )

                    // Render based on view mode
                    when (viewMode) {
                        ViewMode.LIST -> FileListView(sortedRoot)
                        ViewMode.GRID -> FileGridView(sortedRoot)
                        ViewMode.TREE -> FileTreeView(sortedRoot)
                    }
                }
            }
        }
    }
}


@Composable
fun FileItem(node: FileSysNode, modifier: Modifier = Modifier) {
    val icon = if (node.isFolder) {
        folderIcon(node.folderType)
    } else {
        fileIcon(node.name)
    }

    ListItem(
        headlineContent = { Text(node.name, style = MaterialTheme.typography.bodyLarge) },
        supportingContent = {
            Column {
                if (node.description.isNotBlank()) {
                    Text(
                        node.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
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
                    Text(
                        timeText,
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
        modifier = modifier.fillMaxWidth()
    )
}
