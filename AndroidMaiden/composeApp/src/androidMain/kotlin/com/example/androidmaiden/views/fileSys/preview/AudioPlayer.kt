package com.example.androidmaiden.views.fileSys.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

@Composable
actual fun AudioPlayer(
    url: String,
    modifier: Modifier,
    playWhenReady: Boolean
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            this.playWhenReady = playWhenReady
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    // Audio doesn't need a Surface, so we don't return an AndroidView here.
    // The actual UI controls are handled in AudioPreview.kt which will interact with the player state.
    // For simplicity in this step, we just provide the player logic.
}
