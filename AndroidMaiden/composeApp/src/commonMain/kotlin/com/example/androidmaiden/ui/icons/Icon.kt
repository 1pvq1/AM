package com.example.androidmaiden.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.model.FolderType

fun folderTypeIcon(type: FolderType): ImageVector {
    return when (type) {
        FolderType.MUSIC -> Icons.Default.MusicNote
        FolderType.VIDEO -> Icons.Default.Movie
        FolderType.IMAGE -> Icons.Default.Image
        FolderType.DOCUMENT -> Icons.Default.Description
        FolderType.FOLDER -> Icons.Default.Folder
        FolderType.OTHER -> Icons.Default.Folder
    }
}

fun fileTypeIcon(fileName: String): ImageVector {
    val extension = fileName.substringAfterLast('.', "").lowercase()
    return when (extension) {
        "jpg", "jpeg", "png", "gif", "bmp" -> Icons.Default.Image
        "mp4", "mkv", "avi", "mov" -> Icons.Default.Movie
        "mp3", "wav", "ogg", "flac" -> Icons.Default.MusicNote
        "doc", "docx", "pdf", "txt" -> Icons.Default.Description
        "zip", "rar", "7z", "tar", "gz" -> Icons.Default.Archive
        "apk" -> Icons.Default.Android
        else -> Icons.Default.InsertDriveFile
    }
}
