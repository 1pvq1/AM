package com.example.androidmaiden.ui.icons

// androidMain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.screenPages.FileType

@Composable
actual fun fileTypeIcon(type: FileType): ImageVector? {
    return when (type) {
        FileType.FOLDER -> Icons.Default.Folder
        FileType.DOCUMENT -> Icons.Default.Description
        FileType.IMAGE -> Icons.Default.Image
        FileType.MUSIC -> Icons.Default.MusicNote
        FileType.VIDEO -> Icons.Default.VideoLibrary
        FileType.OTHER -> Icons.Default.InsertDriveFile
    }
}
