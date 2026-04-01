package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.screens.fileSystem.analyze.FileItem
import com.example.androidmaiden.ui.icons.folderIcon

/**
 * Supported view modes for file browsing.
 */
enum class ViewMode { LIST, GRID, TREE }

/**
 * Standard list view for file nodes.
 * Supports single click for selection/info and double click for navigation.
 */
@Composable
fun FileListView(
    root: FileSysNode,
    onNodeClick: (FileSysNode) -> Unit = {},
    onNodeDoubleClick: (FileSysNode) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(root.children) { node ->
            FileItem(
                node = node, 
                onClick = { onNodeClick(node) },
                onDoubleClick = { onNodeDoubleClick(node) }
            )
        }
    }
}

/**
 * Grid view for file nodes, useful for media-heavy directories.
 */
@Composable
fun FileGridView(
    root: FileSysNode,
    onNodeClick: (FileSysNode) -> Unit = {},
    onNodeDoubleClick: (FileSysNode) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        root.children.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { node ->
                    FileItem(
                        node = node,
                        modifier = Modifier.weight(1f),
                        onClick = { onNodeClick(node) },
                        onDoubleClick = { onNodeDoubleClick(node) }
                    )
                }
                // Fill empty space if row is not full
                if (row.size < 2) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Recursive tree view for visualizing directory structure.
 */
@Composable
fun FileTreeView(
    root: FileSysNode,
    onNodeClick: (FileSysNode) -> Unit = {},
    onNodeDoubleClick: (FileSysNode) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            FileTreeNode(
                node = root, 
                indent = 0, 
                onNodeClick = onNodeClick,
                onNodeDoubleClick = onNodeDoubleClick
            )
        }
    }
}

/**
 * A single node in the tree view, handles its own expansion state.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileTreeNode(
    node: FileSysNode,
    indent: Int,
    onNodeClick: (FileSysNode) -> Unit,
    onNodeDoubleClick: (FileSysNode) -> Unit
) {
    var expanded by remember { mutableStateOf(indent == 0) } // Root expanded by default

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = (indent * 16).dp)
                .combinedClickable(
                    onClick = {
                        if (node.isFolder) {
                            expanded = !expanded
                        }
                        onNodeClick(node)
                    },
                    onDoubleClick = {
                        onNodeDoubleClick(node)
                    }
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            folderIcon(node.folderType)?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = node.name,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (node.isFolder) {
                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.Default.KeyboardArrowRight
                    },
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }

        if (expanded && node.children.isNotEmpty()) {
            node.children.forEach { child ->
                FileTreeNode(
                    child, 
                    indent + 1, 
                    onNodeClick,
                    onNodeDoubleClick
                )
            }
        }
    }
}
