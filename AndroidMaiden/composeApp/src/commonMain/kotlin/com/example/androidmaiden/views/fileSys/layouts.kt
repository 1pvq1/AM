package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.screenPages.FileItem
import com.example.androidmaiden.views.fileSys.*
import com.example.androidmaiden.ui.icons.folderTypeIcon
import kotlin.collections.chunked
import kotlin.collections.forEach

enum class ViewMode { LIST, GRID, TREE }


@Composable
fun FileListView(root: FileNode) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item{Text("List 视图")}
        items(root.children) { node ->
            FileItem(node)
        }
    }
}

@Composable
fun FileGridView(root: FileNode) {
    // 如果用 Compose Multiplatform，可以用 LazyVerticalGrid
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Grid 视图")
        root.children.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { node -> FileItem(node, Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
fun FileTreeView(root: FileNode) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item{Text("Tree 视图")}

        item {
            FileTreeNode(node = root, indent = 0)
        }
    }
}

@Composable
fun FileTreeNode(node: FileNode, indent: Int) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = (indent * 16).dp).clickable {
                if (node.isFolder) expanded = !expanded
            }, verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            folderTypeIcon(node.folderType)?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = node.name,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(8.dp))

            // 名称
            Text(node.name, style = MaterialTheme.typography.bodyLarge)

            // 展开/折叠指示符
            if (node.isFolder) {
                Spacer(Modifier.width(8.dp))
                Text(
                    if (expanded) "▼" else "▶",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // 子节点
        if (expanded && node.children.isNotEmpty()) {
            node.children.forEach { child ->
                FileTreeNode(child, indent + 1)
            }
        }
    }
}
