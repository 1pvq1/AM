package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.screenPages.FileItem
import com.example.androidmaiden.screenPages.FileNode
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun FileListView(root: FileNode) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(root.children) { node ->
            FileItem(node)
        }
    }
}

@Composable
fun FileGridView(root: FileNode) {
    // 如果用 Compose Multiplatform，可以用 LazyVerticalGrid
    Column {
        Text("Grid 视图占位")
        root.children.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { node -> FileItem(node, Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
fun FileTreeView(root: FileNode, indent: Int = 0) {
    Column {
        FileItem(root, Modifier.padding(start = (indent * 16).dp))
        root.children.forEach { child ->
            FileTreeView(child, indent + 1)
        }
    }
}
