package com.example.androidmaiden.ui.theme.CUSTOM_ONE

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Example Custom Theme Colors (e.g., a "Cyberpunk" or "High Contrast" style)
val md_theme_custom_primary = Color(0xFFD0BCFF)
val md_theme_custom_secondary = Color(0xFFCCC2DC)
val md_theme_custom_tertiary = Color(0xFFEFB8C8)

val LightCustomColorScheme = lightColorScheme(
    primary = md_theme_custom_primary,
    secondary = md_theme_custom_secondary,
    tertiary = md_theme_custom_tertiary
    // ... add more colors as needed
)

val DarkCustomColorScheme = darkColorScheme(
    primary = md_theme_custom_primary,
    secondary = md_theme_custom_secondary,
    tertiary = md_theme_custom_tertiary
)
