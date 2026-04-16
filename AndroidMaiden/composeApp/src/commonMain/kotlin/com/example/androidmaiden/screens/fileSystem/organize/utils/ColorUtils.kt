package com.example.androidmaiden.screens.fileSystem.organize.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {
    /**
     * Parses a hex string (e.g., "#FF0000") into a Compose Color.
     */
    fun parseHexColor(hex: String): Color {
        return try {
            val cleanHex = hex.removePrefix("#")
            val longVal = cleanHex.toLong(16)
            // If the hex is 6 chars, add alpha FF
            val colorInt = if (cleanHex.length <= 6) {
                longVal or 0xFF000000L
            } else {
                longVal
            }
            Color(colorInt)
        } catch (e: Exception) {
            Color.Gray // Fallback
        }
    }

    val tagPalette = listOf(
        "#FF0000", "#00FF00", "#0000FF", "#FFFF00", 
        "#FF00FF", "#00FFFF", "#FFA500", "#800080"
    )
}
