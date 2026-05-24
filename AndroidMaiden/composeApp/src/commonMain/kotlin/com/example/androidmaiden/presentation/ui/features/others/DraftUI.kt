package com.example.androidmaiden.presentation.ui.features.others


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.androidmaiden.domain.model.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Draft UI for audio preview (experimental).
 */
@Composable
private fun AudioPreview(file: FileSysNode) {
    Card(
        modifier = Modifier.padding(32.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MusicNote, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Spacer(Modifier.height(24.dp))
            Text(file.name, style = MaterialTheme.typography.titleLarge, maxLines = 2, textAlign = TextAlign.Center)
            Text("Unknown Artist", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)

            Spacer(Modifier.height(16.dp))
            Slider(value = 0.4f, onValueChange = {})

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.SkipPrevious, null, modifier = Modifier.size(32.dp)) }
                FilledIconButton(onClick = {}, modifier = Modifier.size(64.dp)) {
                    Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(32.dp))
                }
                IconButton(onClick = {}) { Icon(Icons.Default.SkipNext, null, modifier = Modifier.size(32.dp)) }
            }
        }
    }
}

/**
 * Draft UI for video preview (experimental).
 */
@Composable
private fun VideoPreview(file: FileSysNode) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                model = file.path,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(16/9f),
                contentScale = ContentScale.Fit
            )
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Slider(
            value = progress,
            onValueChange = { progress = it },
            modifier = Modifier.padding(horizontal = 32.dp),
            colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { progress = (progress - 0.1f).coerceAtLeast(0f) }) {
                Icon(Icons.Default.Replay10, null, tint = Color.White)
            }
            IconButton(onClick = { isPlaying = !isPlaying }) {
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            IconButton(onClick = { progress = (progress + 0.1f).coerceAtMost(1f) }) {
                Icon(Icons.Default.Forward10, null, tint = Color.White)
            }
        }
    }
}

/**
 * Preview for the draft audio player.
 */
@Preview
@Composable
fun AudioPreview_Preview() {
    AudioPreview(
        FileSysNode(
            name = "Sample Audio.mp3",
            nodeType = NodeType.FILE,
            dataSource = DataSource.MOCK
        )
    )
}

/**
 * Preview for the draft video player.
 */
@Preview
@Composable
fun VideoPreview_Preview() {
    VideoPreview(
        FileSysNode(
            name = "Sample Video.mp4",
            nodeType = NodeType.FILE,
            dataSource = DataSource.MOCK,
            path = "https://example.com/sample.mp4"
        )
    )
}
