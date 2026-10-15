package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.cells

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
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
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.theme.core.LocalFileTypeColors

/**
 * Item cell composable for APK files.
 * Attempts to display the actual app icon if available, falling back to the default Android icon.
 *
 * @param file The APK [FileItem] metadata.
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param modifier Optional layout modifier.
 */
@Composable
fun APKCell(
    file: FileItem,
    viewMode: ViewMode,
    modifier: Modifier = Modifier
) {
    val fileTypeColors = LocalFileTypeColors.current
    val subcategory = remember(file) { FileTypeUtils.getApkSubcategory(file) }

    if (viewMode == ViewMode.GRID) {
        Card(modifier = modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                SubcomposeAsyncImage(
                    model = file.path,
                    contentDescription = file.name,
                    modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Fit
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
                                    imageVector = Icons.Default.Android,
                                    contentDescription = null,
                                    tint = fileTypeColors.apk,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(subcategory.displayName, style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = MaterialTheme.colorScheme.outline)
            }
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { Text("${subcategory.displayName} • ${formatSize(file.size)}") },
            leadingContent = {
                SubcomposeAsyncImage(
                    model = file.path,
                    contentDescription = file.name,
                    modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                    contentScale = ContentScale.Fit
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
                                    imageVector = Icons.Default.Android,
                                    contentDescription = null,
                                    tint = fileTypeColors.apk,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            },
            modifier = modifier
        )
    }
}
