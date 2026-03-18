package com.example.androidmaiden.views.others


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.androidmaiden.model.DataSource
import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.model.NodeType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private  fun AudioPreview(file: FileSysNode) {
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