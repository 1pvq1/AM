package com.example.androidmaiden

import androidx.compose.runtime.compositionLocalOf

enum class ButtonDisplayStyle {
    ICON_AND_TEXT,
    ICON_ONLY,
    TEXT_ONLY
}

val LocalButtonDisplayStyle = compositionLocalOf { ButtonDisplayStyle.ICON_ONLY }
