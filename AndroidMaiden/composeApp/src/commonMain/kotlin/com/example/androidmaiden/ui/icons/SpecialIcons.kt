package com.example.androidmaiden.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.model.FolderType

enum class ToolbarIcon { LIST, GRID, TREE, SORT }

@Composable
fun toolbarIcon(icon: ToolbarIcon): ImageVector {
    return when (icon) {
        ToolbarIcon.LIST -> Icons.Default.List
        ToolbarIcon.GRID -> Icons.Default.GridView
        ToolbarIcon.TREE -> Icons.Default.AccountTree
        ToolbarIcon.SORT -> Icons.Default.Sort
    }
}

@Composable
fun folderTypeIcon(type: FolderType): ImageVector {
    return when (type) {
        FolderType.FOLDER -> Icons.Default.Folder
        FolderType.DOCUMENT -> Icons.Default.Description
        FolderType.IMAGE -> Icons.Default.Image
        FolderType.MUSIC -> Icons.Default.MusicNote
        FolderType.VIDEO -> Icons.Default.VideoLibrary
        FolderType.OTHER -> Icons.Default.InsertDriveFile
    }
}

enum class M3Icon { ADD }

@Composable
fun m3Icon(icon: M3Icon): ImageVector {
    return when (icon) {
        M3Icon.ADD -> Icons.Default.Add
    }
}
