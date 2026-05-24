package com.example.androidmaiden.presentation.ui.features.fileSys.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.androidmaiden.data.local.FileMetadata

/**
 * Composable for previewing video files.
 */
@Composable
fun VideoPreview(file: FileMetadata) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val path = file.path
        VideoPlayer(
            url = path,
            modifier = Modifier.fillMaxSize()
        )
    }
}
