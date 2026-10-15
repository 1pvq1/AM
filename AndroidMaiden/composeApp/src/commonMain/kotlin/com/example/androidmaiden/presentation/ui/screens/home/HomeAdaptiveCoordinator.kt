package com.example.androidmaiden.presentation.ui.screens.home

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.features.character.CharacterLayout
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Adaptive Coordinator for the Home screen.
 * Responsible for deciding the layout structure based on window size.
 */
@Composable
fun HomeAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
    showCharacterDialog: Boolean,
    onShowCharacterDialogChange: (Boolean) -> Unit,
    characterLayout: CharacterLayout,
    onCharacterLayoutChange: (CharacterLayout) -> Unit,
    dialogText: String
) {
    val isExpanded = windowSizeClass.widthCategory == WindowSizeCategory.Expanded
    val isMedium = windowSizeClass.widthCategory == WindowSizeCategory.Medium

    HomeContent(
        isExpanded = isExpanded,
        isMedium = isMedium,
        showCharacterDialog = showCharacterDialog,
        onShowCharacterDialogChange = onShowCharacterDialogChange,
        characterLayout = characterLayout,
        onCharacterLayoutChange = onCharacterLayoutChange,
        dialogText = dialogText
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun HomeAdaptiveCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            HomeAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun HomeAdaptiveCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            HomeAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun HomeAdaptiveCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            HomeAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
        }
    }
}

@Composable
private fun HomeAdaptiveCoordinatorPreviewHelper(widthCategory: WindowSizeCategory) {
    HomeAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        showCharacterDialog = true,
        onShowCharacterDialogChange = { },
        characterLayout = CharacterLayout.Horizontal,
        onCharacterLayoutChange = { },
        dialogText = "Hello from Preview!"
    )
}

