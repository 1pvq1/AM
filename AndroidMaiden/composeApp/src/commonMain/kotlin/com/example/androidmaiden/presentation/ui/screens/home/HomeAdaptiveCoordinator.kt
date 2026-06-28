package com.example.androidmaiden.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.features.character.CharacterLayout

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
