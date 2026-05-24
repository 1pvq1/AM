package com.example.androidmaiden.presentation.ui.features.fileSys.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    playWhenReady: Boolean = true
)

@Composable
expect fun AudioPlayer(
    url: String,
    modifier: Modifier = Modifier,
    playWhenReady: Boolean = true
)
