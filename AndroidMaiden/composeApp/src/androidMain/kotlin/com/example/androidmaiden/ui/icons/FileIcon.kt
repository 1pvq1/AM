package com.example.androidmaiden.ui.icons

// androidMain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.views.fileSys.FolderType

@Composable
actual fun folderTypeIcon(type: FolderType): ImageVector? {
    return when (type) {
        FolderType.FOLDER -> Icons.Default.Folder
        FolderType.DOCUMENT -> Icons.Default.Description
        FolderType.IMAGE -> Icons.Default.Image
        FolderType.MUSIC -> Icons.Default.MusicNote
        FolderType.VIDEO -> Icons.Default.VideoLibrary
        FolderType.OTHER -> Icons.Default.InsertDriveFile
    }
}

@Composable
actual fun toolbarIcon(icon: ToolbarIcon): ImageVector? {
    return when (icon) {
        ToolbarIcon.LIST -> Icons.Default.List
        ToolbarIcon.GRID -> Icons.Default.GridView
        ToolbarIcon.TREE -> Icons.Default.AccountTree
        ToolbarIcon.SORT -> Icons.Default.Sort
    }
}

//@Composable
//actual fun m3Icons(icon: m3): ImageVector? {
//    return when (icon) {
//        m3Icons.ADD -> Icons.Default.Add
//    }
//}
