package com.example.androidmaiden.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.features.character.CharacterLayout
import com.example.androidmaiden.presentation.ui.theme.AppTheme
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

@Preview(name = "Compact", showBackground = true)
@Composable
fun HomeAdaptiveCoordinatorCompactPreview() {
    AppTheme {
        HomeAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
    }
}

@Preview(name = "Medium", showBackground = true)
@Composable
fun HomeAdaptiveCoordinatorMediumPreview() {
    AppTheme {
        HomeAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
    }
}

@Preview(name = "Expanded", showBackground = true)
@Composable
fun HomeAdaptiveCoordinatorExpandedPreview() {
    AppTheme {
        HomeAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
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
