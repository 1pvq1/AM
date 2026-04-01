package com.example.androidmaiden.views.fileSys.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.androidmaiden.data.FileMetadata

@Composable
fun VideoPreview(file: FileMetadata) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val path = file.path
        if (path != null) {
            VideoPlayer(
                url = path,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text("File path not available", color = Color.White)
        }
    }
}
