package com.example.androidmaiden.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.androidmaiden.ui.theme.DefaultTheme.DarkDefaultColorScheme
import com.example.androidmaiden.ui.theme.DefaultTheme.LightDefaultColorScheme
import com.example.androidmaiden.ui.theme.MaidenTheme.DarkMaidenColorScheme
import com.example.androidmaiden.ui.theme.MaidenTheme.LightMaidenColorScheme
import com.example.androidmaiden.ui.theme.TokyoNightTheme.DarkTokyoNightColorScheme
import com.example.androidmaiden.ui.theme.TokyoNightTheme.LightTokyoNightColorScheme
import com.example.androidmaiden.ui.theme.core.*

@Composable
expect fun rememberDynamicColorScheme(darkTheme: Boolean): ColorScheme?

expect val isDynamicColorSupported: Boolean
@Composable
fun AppTheme(
    themeType: AppThemeType = AppThemeType.DEFAULT,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    useDynamicColor: Boolean = false,
    buttonDisplayStyle: ButtonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val dynamicColorScheme = rememberDynamicColorScheme(darkTheme)
    
    val colorScheme = if (useDynamicColor && dynamicColorScheme != null) {
        dynamicColorScheme
    } else {
        when (themeType) {
            AppThemeType.DEFAULT -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
            AppThemeType.MAIDEN -> if (darkTheme) DarkMaidenColorScheme else LightMaidenColorScheme
            AppThemeType.TOKYO_NIGHT -> if (darkTheme) DarkTokyoNightColorScheme else LightTokyoNightColorScheme
            AppThemeType.CUSTOM_ONE -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme // Placeholder

        }
    }

    CompositionLocalProvider(
        LocalButtonDisplayStyle provides buttonDisplayStyle,
        LocalFileTypeColors provides DefaultFileTypeColors,
        LocalAppExtraShapes provides AppExtraShapes()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}
