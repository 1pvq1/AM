package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.cells

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.androidmaiden.core.util.*
import com.example.androidmaiden.domain.model.AudioSubcategory
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.theme.core.LocalFileTypeColors

/**
 * Item cell composable for audio files with smart subcategory display.
 * Displays embedded album cover artwork if available; otherwise falls back to a category-specific default audio icon.
 *
 * @param file The audio [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun AudioCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    val fileTypeColors = LocalFileTypeColors.current
    val subcategory = remember(file) { FileTypeUtils.getAudioSubcategory(file) }
    val isRecording = subcategory == AudioSubcategory.RECORDINGS

    val defaultIcon = when (subcategory) {
        AudioSubcategory.RECORDINGS -> Icons.Default.Mic
        AudioSubcategory.RINGTONES -> Icons.Default.Notifications
        AudioSubcategory.PODCASTS_AUDIOBOOKS -> Icons.AutoMirrored.Filled.MenuBook
        else -> Icons.Default.Audiotrack
    }
    val defaultTint = if (isRecording) MaterialTheme.colorScheme.secondary else fileTypeColors.audio

    if (viewMode == ViewMode.GRID) {
        Card(modifier = modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                SubcomposeAsyncImage(
                    model = file.path,
                    contentDescription = file.name,
                    modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = defaultIcon,
                                    contentDescription = null,
                                    tint = defaultTint,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(file.artist ?: subcategory.displayName, style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = MaterialTheme.colorScheme.outline, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            supportingContent = {
                val author = file.artist ?: subcategory.displayName
                val album = file.album ?: ""
                val albumText = if (album.isNotBlank()) " • $album" else ""
                val bitrateText = file.bitrate?.let { " • ${it / 1000}kbps" } ?: ""
                val durationText = file.duration?.let { " • ${formatDuration(it)}" } ?: ""
                Text("$author$albumText$bitrateText$durationText • ${formatSize(file.size)}")
            },
            leadingContent = {
                SubcomposeAsyncImage(
                    model = file.path,
                    contentDescription = file.name,
                    modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                    contentScale = ContentScale.Crop
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = defaultIcon,
                                    contentDescription = null,
                                    tint = defaultTint,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            },
            trailingContent = { 
                val durationText = file.duration?.let { formatDuration(it) } ?: "00:00"
                Text(durationText, style = MaterialTheme.typography.labelSmall) 
            },
            modifier = modifier
        )
    }
}
