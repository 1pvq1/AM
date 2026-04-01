package com.example.androidmaiden.screens.fileSystem.classify

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.views.fileSys.preview.AudioPreview
import com.example.androidmaiden.views.fileSys.preview.ImagePreview
import com.example.androidmaiden.views.fileSys.preview.VideoPreview
import com.example.androidmaiden.utils.FileTypeUtils


/**
 * Enhanced file previewer.
 * Features:
 * 1. Image: Zoom, Rotate (Manual & Gesture), Drag (with auto-reset), and Full-screen toggle.
 * 2. Media: Video/Audio playback simulation.
 * 3. Navigation: Shows parent folder name in title.
 * 4. Actions: Bottom action bar for tools like rotation.
 */
@Composable
fun FilePreviewOverlay(
    file: FileMetadata,
    onDismiss: () -> Unit
) {
    val category = remember(file.name) { FileTypeUtils.getExtensionType(file.name) }
    
    var isUiVisible by remember { mutableStateOf(true) }
    var isBottomBarVisible by remember { mutableStateOf(false) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) { isVisible = true }

    val parentFolder = remember(file.path) {
        val path = file.path?.replace("\\", "/") ?: ""
        path.substringBeforeLast("/", "").substringAfterLast("/", "").ifBlank { "Root" }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.8f, animationSpec = tween(300)),
            exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.8f, animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Main Content
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (category) {
                        "Images" -> ImagePreview(file, rotation) {
                            isUiVisible = !isUiVisible
                            if (!isUiVisible) isBottomBarVisible = false
                        }
                        "Videos" -> VideoPreview(file)
                        "Audio" -> AudioPreview(file)
                        else -> GenericPreview(file)
                    }
                }

                // Top Toolbar
                AnimatedVisibility(
                    visible = isUiVisible,
                    enter = slideInVertically { -it } + fadeIn(),
                    exit = slideOutVertically { -it } + fadeOut()
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth().statusBarsPadding()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                            Text(
                                text = parentFolder,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
                            )
                            
                            IconButton(onClick = { isBottomBarVisible = !isBottomBarVisible }) {
                                Icon(
                                    imageVector = if (isBottomBarVisible) Icons.Default.ExpandMore else Icons.Default.MoreVert,
                                    contentDescription = "More",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                // Bottom Action Bar
                AnimatedVisibility(
                    visible = isUiVisible && isBottomBarVisible,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (category == "Images") {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { rotation = (rotation + 90f) % 360f },
                                        modifier = Modifier.size(48.dp).background(Color.White.copy(0.1f), CircleShape)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.RotateRight, null, tint = Color.White)
                                    }
                                    Text("Rotate", color = Color.White, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                                }
                            }
                            // Add more actions here if needed
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GenericPreview(file: FileMetadata) {
    Card(
        modifier = Modifier.padding(32.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Text(file.name, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text("Preview under development", color = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { /* Intent implementation */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, null)
                Spacer(Modifier.width(8.dp))
                Text("Open in System App")
            }
        }
    }
}
