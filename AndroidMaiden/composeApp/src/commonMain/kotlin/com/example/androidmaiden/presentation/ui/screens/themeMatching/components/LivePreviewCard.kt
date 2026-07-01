package com.example.androidmaiden.presentation.ui.screens.themeMatching.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.themematching.model.CharacterAction
import com.example.androidmaiden.domain.themematching.model.SleepParticle
import com.example.androidmaiden.presentation.ui.features.character.CharacterIllustration
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sin

/**
 * Live Animation Box that renders the pixel art character illustration
 * and applies translations/rotations corresponding to the active action.
 *
 * @param modifier Modifier for root Card layouts.
 * @param activeAction The currently selected CharacterAction.
 * @param isPlaying Whether the animation sequence is actively playing.
 * @param speedScale The multiplier speed factor for animations.
 */
@Composable
fun LivePreviewCard(
    modifier: Modifier = Modifier,
    activeAction: CharacterAction,
    isPlaying: Boolean,
    speedScale: Float
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Background Grid Lines for a pixelated blueprint view
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gridColor = Color.LightGray.copy(alpha = 0.15f)
                val strokeWidth = 1f
                val spacing = 20.dp.toPx()
                
                // Draw vertical lines
                var x = 0f
                while (x < size.width) {
                    drawLine(gridColor, start = Offset(x, 0f), end = Offset(x, size.height), strokeWidth = strokeWidth)
                    x += spacing
                }
                
                // Draw horizontal lines
                var y = 0f
                while (y < size.height) {
                    drawLine(gridColor, start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = strokeWidth)
                    y += spacing
                }
            }

            // Animate character properties
            val transition = rememberInfiniteTransition(label = "CharacterPreviewAnim")
            
            // Base values modulated by playing state and speed multiplier
            val playFactor = if (isPlaying) 1f else 0f
            val baseTimeScale = 1000 / speedScale

            // 1. Walking / Running / Idle Bobbing
            val bobValue by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween((baseTimeScale * 0.8f).toInt(), easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bob"
            )

            val rotationValue by transition.animateFloat(
                initialValue = -1f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween((baseTimeScale * 1.2f).toInt(), easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "rotate"
            )

            // Setup stateful custom animations for JUMPING, SLEEPING, WAVING
            var jumpOffset by remember { mutableStateOf(0f) }
            var jumpScaleX by remember { mutableStateOf(1f) }
            var jumpScaleY by remember { mutableStateOf(1f) }
            var jumpRotation by remember { mutableStateOf(0f) }
            
            val coroutineScope = rememberCoroutineScope()

            // Keep track of sleep particles (Zzz)
            val sleepParticles = remember { mutableStateListOf<SleepParticle>() }

            // Trigger Jumping Animation Coroutine Loop
            if (activeAction == CharacterAction.JUMPING && isPlaying) {
                LaunchedEffect(activeAction, speedScale) {
                    while (true) {
                        val duration = (500 / speedScale).toLong()
                        // 1. Squash down
                        animate(1f, 0.82f, animationSpec = tween((duration * 0.3f).toInt(), easing = FastOutSlowInEasing)) { value, _ ->
                            jumpScaleY = value
                            jumpScaleX = 2f - value
                        }
                        // 2. Launch up
                        coroutineScope.launch {
                            animate(0f, -80f, animationSpec = tween((duration * 0.8f).toInt(), easing = LinearOutSlowInEasing)) { value, _ ->
                                jumpOffset = value
                            }
                        }
                        animate(0.82f, 1.2f, animationSpec = tween((duration * 0.4f).toInt(), easing = LinearOutSlowInEasing)) { value, _ ->
                            jumpScaleY = value
                            jumpScaleX = 2f - value
                        }
                        // 3. Flip mid-air
                        animate(0f, 360f, animationSpec = tween((duration * 0.8f).toInt(), easing = FastOutSlowInEasing)) { value, _ ->
                            jumpRotation = value
                        }
                        // 4. Descend & squash on impact
                        coroutineScope.launch {
                            animate(-80f, 0f, animationSpec = tween((duration * 0.8f).toInt(), easing = FastOutLinearInEasing)) { value, _ ->
                                jumpOffset = value
                            }
                        }
                        animate(1.2f, 0.8f, animationSpec = tween((duration * 0.8f).toInt(), easing = FastOutLinearInEasing)) { value, _ ->
                            jumpScaleY = value
                            jumpScaleX = 2f - value
                        }
                        // 5. Recover to normal
                        animate(0.8f, 1.0f, animationSpec = tween((duration * 0.4f).toInt(), easing = FastOutSlowInEasing)) { value, _ ->
                            jumpScaleY = value
                            jumpScaleX = 2f - value
                        }
                        delay((1200 / speedScale).toLong())
                    }
                }
            } else {
                jumpOffset = 0f
                jumpScaleX = 1f
                jumpScaleY = 1f
                jumpRotation = 0f
            }

            // Trigger Sleeping Particles loop
            if (activeAction == CharacterAction.SLEEPING && isPlaying) {
                LaunchedEffect(activeAction, speedScale) {
                    var idCounter = 0
                    while (true) {
                        sleepParticles.add(SleepParticle(idCounter++, 0f, 0f, 1f))
                        delay((800 / speedScale).toLong())
                    }
                }
                
                // Animate existing particles upwards
                LaunchedEffect(isPlaying, speedScale) {
                    while (true) {
                        withFrameMillis {
                            val iterator = sleepParticles.listIterator()
                            while (iterator.hasNext()) {
                                val particle = iterator.next()
                                val nextY = particle.y - (1.5f * speedScale)
                                val nextX = particle.x + (sin(nextY / 15f) * 0.5f)
                                val nextAlpha = (1f + (nextY / 120f)).coerceIn(0f, 1f)
                                if (nextAlpha <= 0f) {
                                    iterator.remove()
                                } else {
                                    iterator.set(particle.copy(x = nextX, y = nextY, alpha = nextAlpha))
                                }
                            }
                        }
                    }
                }
            } else {
                sleepParticles.clear()
            }

            // Calculations based on active animations
            var animYOffset = 0f
            var animXOffset = 0f
            var animRotation = 0f
            var animScaleX = 1f
            var animScaleY = 1f

            if (isPlaying) {
                when (activeAction) {
                    CharacterAction.IDLE -> {
                        // Slow rhythmic floating and breathing
                        animYOffset = bobValue * 8f
                        animScaleY = 1f - (bobValue * 0.03f)
                        animScaleX = 1f + (bobValue * 0.02f)
                    }
                    CharacterAction.WALKING -> {
                        // Bouncy translation and rotation
                        animYOffset = -abs(bobValue * 14f)
                        animRotation = rotationValue * 5f
                        animXOffset = rotationValue * 6f
                    }
                    CharacterAction.RUNNING -> {
                        // Faster, forward-tilted bounce with slight horizontal vibration
                        animYOffset = -abs(bobValue * 18f)
                        animRotation = 12f + (rotationValue * 2f) // Tilted forward
                        animXOffset = rotationValue * 3f
                        animScaleY = 1.04f - (bobValue * 0.08f)
                        animScaleX = 0.96f + (bobValue * 0.08f)
                    }
                    CharacterAction.WAVING -> {
                        // Character swaying side-to-side, pivot at feet
                        animRotation = rotationValue * 10f
                        animYOffset = bobValue * 3f
                    }
                    CharacterAction.JUMPING -> {
                        // Controlled by custom animation loop
                        animYOffset = jumpOffset
                        animScaleX = jumpScaleX
                        animScaleY = jumpScaleY
                        animRotation = jumpRotation
                    }
                    CharacterAction.SLEEPING -> {
                        // Tilted and breathing extremely slowly
                        animRotation = -15f
                        animScaleY = 1f - (bobValue * 0.04f)
                        animScaleX = 1f + (bobValue * 0.02f)
                        animYOffset = bobValue * 2f
                    }
                }
            }

            // Draw Sleep Particles (Zzz)
            sleepParticles.forEach { particle ->
                Box(
                    modifier = Modifier
                        .offset(x = (40.dp + particle.x.dp), y = (-40.dp + particle.y.dp))
                        .graphicsLayer(alpha = particle.alpha)
                ) {
                    Text(
                        text = if (particle.id % 2 == 0) "Z" else "z",
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        fontSize = (12 + (particle.id % 3) * 4).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Draw Waving Bubble Emoji
            if (activeAction == CharacterAction.WAVING && isPlaying) {
                val waveBubbleY by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = -8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(400, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "waveBubble"
                )

                Box(
                    modifier = Modifier
                        .offset(x = 60.dp, y = (-70.dp + waveBubbleY.dp))
                        .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "👋 HELLO!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Character Container applying computed layout manipulations
            Box(
                modifier = Modifier
                    .size(160.dp, 160.dp)
                    .graphicsLayer(
                        translationY = animYOffset * 2f,
                        translationX = animXOffset * 2f,
                        rotationZ = animRotation,
                        scaleX = animScaleX,
                        scaleY = animScaleY,
                        transformOrigin = TransformOrigin(0.5f, 0.9f) // Rotate and scale from the feet/bottom of the sprite
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Background shadow beneath the character
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .width(70.dp)
                        .height(8.dp)
                        .graphicsLayer(
                            alpha = (0.35f + (animYOffset / 80f)).coerceIn(0.1f, 0.45f),
                            scaleX = 1f - (animYOffset / 100f)
                        )
                        .background(Color.Black, shape = CircleShape)
                )

                CharacterIllustration(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun LivePreviewCardPreview() {
    AppTheme {
        LivePreviewCard(
            activeAction = CharacterAction.IDLE,
            isPlaying = true,
            speedScale = 1.0f
        )
    }
}
