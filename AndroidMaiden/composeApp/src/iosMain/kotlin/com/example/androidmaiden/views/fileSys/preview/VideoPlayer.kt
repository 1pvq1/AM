package com.example.androidmaiden.views.fileSys.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    playWhenReady: Boolean
) {
    Box(
        modifier = modifier.background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Video playback not yet supported on iOS", color = Color.White)
    }
}

@Composable
actual fun AudioPlayer(
    url: String,
    modifier: Modifier,
    playWhenReady: Boolean
) {
}
