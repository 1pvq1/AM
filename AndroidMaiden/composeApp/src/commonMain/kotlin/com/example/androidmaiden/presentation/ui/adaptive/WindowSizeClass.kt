package com.example.androidmaiden.presentation.ui.adaptive

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Enumeration of window size categories.
 */
enum class WindowSizeCategory {
    Compact, Medium, Expanded
}

/**
 * Data class representing the window size class of the current window.
 */
@Immutable
data class WindowSizeClass(
    val widthCategory: WindowSizeCategory,
    val heightCategory: WindowSizeCategory
) {
    companion object {
        /**
         * Calculates the [WindowSizeClass] based on the width and height in Dp.
         */
        fun calculate(width: Dp, height: Dp): WindowSizeClass {
            val widthCategory = when {
                width < 600.dp -> WindowSizeCategory.Compact
                width < 840.dp -> WindowSizeCategory.Medium
                else -> WindowSizeCategory.Expanded
            }
            val heightCategory = when {
                height < 480.dp -> WindowSizeCategory.Compact
                height < 900.dp -> WindowSizeCategory.Medium
                else -> WindowSizeCategory.Expanded
            }
            return WindowSizeClass(widthCategory, heightCategory)
        }
    }
}

/**
 * CompositionLocal for [WindowSizeClass].
 * Defaults to Compact/Compact if not provided.
 */
val LocalWindowSizeClass = compositionLocalOf {
    WindowSizeClass(WindowSizeCategory.Compact, WindowSizeCategory.Compact)
}
