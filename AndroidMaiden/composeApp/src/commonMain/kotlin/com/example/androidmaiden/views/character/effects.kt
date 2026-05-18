package com.example.androidmaiden.views.character

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Applies a continuous floating animation to its content.
 * Improved with natural easing for a "weightless" effect.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param content The composable content to be animated.
 */
@Composable
fun AnimatedFloating(
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floatAnim")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )
    content(modifier.then(Modifier.offset(y = offsetY.dp)))
}
