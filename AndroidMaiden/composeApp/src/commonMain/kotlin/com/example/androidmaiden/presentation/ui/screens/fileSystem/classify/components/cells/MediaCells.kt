package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.cells

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.androidmaiden.core.util.*
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode

/**
 * Item cell composable for image files in Grid or List view modes.
 *
 * @param file The image [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun ImagesCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    val subcategory = remember(file) { FileTypeUtils.getImageSubcategory(file) }
    if (viewMode == ViewMode.GRID) {
        Card(shape = MaterialTheme.shapes.small, modifier = modifier) {
            Box {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                ) {
                    Text(
                        text = file.name,
                        color = Color.White,
                        fontSize = 10.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { 
                val resolution = if (file.width != null && file.height != null) " • ${file.width}x${file.height}" else ""
                Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}$resolution • ${subcategory.displayName}") 
            },
            leadingContent = {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                    contentScale = ContentScale.Crop
                )
            },
            modifier = modifier
        )
    }
}

/**
 * Item cell composable for video files with duration and resolution badges.
 *
 * @param file The video [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun VideosCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    val subcategory = remember(file) { FileTypeUtils.getVideoSubcategory(file) }
    if (viewMode == ViewMode.GRID) {
        Card(modifier = modifier.fillMaxWidth()) {
            Box {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(16/9f).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center).size(32.dp),
                    tint = Color.White.copy(alpha = 0.8f)
                )
                
                val durationText = remember(file.duration) {
                    file.duration?.let { formatDuration(it) } ?: "00:00"
                }
                
                val resolutionText = remember(file.width, file.height) {
                    if (file.width != null && file.height != null) {
                        when {
                            file.height >= 2160 -> "4K"
                            file.height >= 1080 -> "FHD"
                            file.height >= 720 -> "HD"
                            else -> "${file.width}x${file.height}"
                        }
                    } else "Video"
                }

                Text(
                    text = "$resolutionText • $durationText",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                        .background(Color.Black.copy(0.6f), MaterialTheme.shapes.extraSmall).padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(file.name, modifier = Modifier.padding(4.dp), maxLines = 1, style = MaterialTheme.typography.labelSmall)
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { 
                val durationText = file.duration?.let { " • ${formatDuration(it)}" } ?: ""
                val resolution = if (file.width != null && file.height != null) " • ${file.width}x${file.height}" else ""
                Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}$durationText$resolution • ${subcategory.displayName}") 
            },
            leadingContent = {
                Box {
                    AsyncImage(
                        model = file.path,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp).align(Alignment.Center),
                        tint = Color.White
                    )
                }
            },
            modifier = modifier
        )
    }
}
