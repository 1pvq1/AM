package com.example.androidmaiden.ui.theme.core

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class AppThemeType {
    DEFAULT,
    MAIDEN,
    TOKYO_NIGHT,
    CUSTOM_ONE // sample
}

enum class ButtonDisplayStyle {
    ICON_AND_TEXT,
    ICON_ONLY,
    TEXT_ONLY
}

val LocalButtonDisplayStyle = compositionLocalOf { ButtonDisplayStyle.ICON_ONLY }

data class FileTypeColors(
    val image: Color,
    val video: Color,
    val audio: Color,
    val document: Color,
    val archive: Color,
    val apk: Color,
    val other: Color,
    val pdf: Color = Color(0xFFF44336),
    val doc: Color = Color(0xFF2196F3),
    val xls: Color = Color(0xFF4CAF50),
    val txt: Color = Color(0xFF9C27B0)
)

val DefaultFileTypeColors = FileTypeColors(
    image = Color(0xFF4CAF50),
    video = Color(0xFF2196F3),
    audio = Color(0xFFE91E63),
    document = Color(0xFFFF9800),
    archive = Color(0xFFFF9800), // Reusing document color or a separate one
    apk = Color(0xFF3DDC84),
    other = Color(0xFF9E9E9E)
)

val LocalFileTypeColors = compositionLocalOf { DefaultFileTypeColors }
