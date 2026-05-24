package com.example.androidmaiden.presentation.ui.theme.core

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class AppExtraShapes(
    val dialogBubble: Shape = RoundedCornerShape(12.dp),
    val characterBox: Shape = RoundedCornerShape(16.dp),
    val cardClickable: Shape = RoundedCornerShape(12.dp)
)

val LocalAppExtraShapes = staticCompositionLocalOf { AppExtraShapes() }

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
