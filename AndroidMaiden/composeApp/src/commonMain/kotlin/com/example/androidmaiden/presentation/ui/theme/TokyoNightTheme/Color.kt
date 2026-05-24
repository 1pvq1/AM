package com.example.androidmaiden.presentation.ui.theme.TokyoNightTheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Tokyo Night Theme
// Dark
val tn_dark_background = Color(0xFF1A1B26)
val tn_dark_surface = Color(0xFF24283B)
val tn_dark_primary = Color(0xFF7AA2F7)
val tn_dark_onPrimary = Color(0xFF1A1B26)
val tn_dark_secondary = Color(0xFFBB9AF7)
val tn_dark_onSecondary = Color(0xFF1A1B26)
val tn_dark_tertiary = Color(0xFF7DCFFF)
val tn_dark_error = Color(0xFFF7768E)
val tn_dark_onSurface = Color(0xFFC0CAF5)
val tn_dark_onBackground = Color(0xFFC0CAF5)

// Light (Tokyo Night Day)
val tn_light_background = Color(0xFFD5D6DB)
val tn_light_surface = Color(0xFFCBCCD1)
val tn_light_primary = Color(0xFF3760BF)
val tn_light_onPrimary = Color(0xFFFFFFFF)
val tn_light_secondary = Color(0xFF587531)
val tn_light_onSecondary = Color(0xFFFFFFFF)
val tn_light_tertiary = Color(0xFF38507A)
val tn_light_error = Color(0xFF8C4351)
val tn_light_onSurface = Color(0xFF343B58)
val tn_light_onBackground = Color(0xFF343B58)

val DarkTokyoNightColorScheme = darkColorScheme(
    primary = tn_dark_primary,
    onPrimary = tn_dark_onPrimary,
    primaryContainer = Color(0xFF2E3C64),
    onPrimaryContainer = Color(0xFF7AA2F7),
    secondary = tn_dark_secondary,
    onSecondary = tn_dark_onSecondary,
    secondaryContainer = Color(0xFF414868),
    onSecondaryContainer = Color(0xFFBB9AF7),
    tertiary = tn_dark_tertiary,
    onTertiary = Color(0xFF1A1B26),
    tertiaryContainer = Color(0xFF24283B),
    onTertiaryContainer = Color(0xFF7DCFFF),
    error = tn_dark_error,
    onError = Color(0xFF1A1B26),
    background = tn_dark_background,
    onBackground = tn_dark_onBackground,
    surface = tn_dark_surface,
    onSurface = tn_dark_onSurface,
    surfaceVariant = Color(0xFF292E42),
    onSurfaceVariant = Color(0xFF565F89),
    outline = Color(0xFF565F89)
)

val LightTokyoNightColorScheme = lightColorScheme(
    primary = tn_light_primary,
    onPrimary = tn_light_onPrimary,
    primaryContainer = Color(0xFFB1C4F2),
    onPrimaryContainer = tn_light_primary,
    secondary = tn_light_secondary,
    onSecondary = tn_light_onSecondary,
    secondaryContainer = Color(0xFFCFE0B7),
    onSecondaryContainer = tn_light_secondary,
    tertiary = tn_light_tertiary,
    onTertiary = tn_light_onPrimary,
    background = tn_light_background,
    onBackground = tn_light_onBackground,
    surface = tn_light_surface,
    onSurface = tn_light_onSurface,
    surfaceVariant = Color(0xFFB4B5B9),
    onSurfaceVariant = Color(0xFF444B6A),
    outline = Color(0xFF444B6A)
)
