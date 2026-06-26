package com.example.androidmaiden.presentation.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize

/**
 * Remembers the window size class for the current window.
 * This implementation uses LocalWindowInfo which is available in commonMain.
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    
    val size = windowInfo.containerSize
    val widthDp = with(density) { size.width.toDp() }
    val heightDp = with(density) { size.height.toDp() }
    
    return WindowSizeClass.calculate(widthDp, heightDp)
}
