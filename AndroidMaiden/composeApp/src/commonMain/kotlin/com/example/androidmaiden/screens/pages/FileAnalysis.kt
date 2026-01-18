package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.mods.RequestStoragePermission
import com.example.androidmaiden.mods.listFiles
import com.example.androidmaiden.model.*
import com.example.androidmaiden.ui.icons.fileTypeIcon
import com.example.androidmaiden.ui.icons.folderTypeIcon
import com.example.androidmaiden.utils.formatDateTime
import com.example.androidmaiden.views.eg.simFileNode
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.views.panel.FileAnalysisToolbar
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
fun FileAnalysisScreen(onNavigateUp: () -> Unit = {}) {

    var viewMode by remember { mutableStateOf(ViewMode.LIST) }
    var sortMode by remember { mutableStateOf(SortMode.NAME) }
    var sortOrder by remember { mutableStateOf(SortOrder.ASC) }
    var useMock by remember { mutableStateOf(true) }

    val root = if (useMock) simFileNode()
    else FileNode(
        name = "/",
        nodeType = NodeType.FOLDER,
        folderType = FolderType.FOLDER,
        dataSource = DataSource.REAL,
        children = listFiles("/storage/emulated/0"), // Public storage root
        lastModified = Clock.System.now().toEpochMilliseconds(),
        description = "Real device root",
        path = "/storage/emulated/0"
    )

    // Request storage permission when the page is entered
    RequestStoragePermission()

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
                onUseMockChange = { useMock = it },
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
                else stringResource(id = "current_data_type_real"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(16.dp))

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


@Composable
fun FileItem(node: FileNode, modifier: Modifier = Modifier) {
    val icon = if (node.isFolder) {
        folderTypeIcon(node.folderType)
    } else {
        fileTypeIcon(node.name)
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
                val timeText = node.lastModified?.let { formatDateTime(it) } ?: stringResource(id = "unknown_time")
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
