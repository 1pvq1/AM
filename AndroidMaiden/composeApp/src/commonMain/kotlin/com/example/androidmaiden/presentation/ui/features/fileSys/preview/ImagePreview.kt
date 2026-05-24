package com.example.androidmaiden.presentation.ui.features.fileSys.preview

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.androidmaiden.data.local.FileMetadata
import kotlin.math.*

/**
 * Composable for previewing images with zoom, pan, and rotation support.
 */
@Composable
fun ImagePreview(file: FileMetadata, manualRotation: Float, onToggleUi: () -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var gestureRotation by remember { mutableFloatStateOf(0f) }

    val totalRotation = manualRotation + gestureRotation

    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        gestureRotation += rotationChange

        // Fix for abnormal dragging after rotation:
        // Translate the screen-space offset delta into the layer's local coordinate system.
        val angleRad = -totalRotation * (PI / 180f).toFloat()
        val cosA = cos(angleRad)
        val sinA = sin(angleRad)

        val rotatedOffset = Offset(
            x = offsetChange.x * cosA - offsetChange.y * sinA,
            y = offsetChange.x * sinA + offsetChange.y * cosA
        )
        offset += rotatedOffset
    }

    // Auto-reset when zoomed out
    LaunchedEffect(scale) {
        if (scale <= 1f) {
            val startOffset = offset
            val startGestureRotation = gestureRotation
            animate(0f, 1f, animationSpec = tween(400, easing = FastOutSlowInEasing)) { value, _ ->
                offset = Offset(
                    lerp(startOffset.x, 0f, value),
                    lerp(startOffset.y, 0f, value)
                )
                gestureRotation = lerp(startGestureRotation, 0f, value)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onToggleUi() },
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                        } else {
                            scale = 2.5f
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = file.path,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                    rotationZ = totalRotation
                }
                .transformable(state = state),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * Linearly interpolates between two values.
 */
private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}
