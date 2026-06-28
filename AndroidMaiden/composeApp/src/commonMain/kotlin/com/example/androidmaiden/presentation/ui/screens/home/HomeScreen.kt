package com.example.androidmaiden.presentation.ui.screens.home

import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.features.character.CharacterLayout
import com.example.androidmaiden.platform.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateful Entry Point for the Home screen.
 */
@Preview
@Composable
fun HomeScreen() {
    val windowSizeClass = LocalWindowSizeClass.current
    
    // UI State
    var showCharacterDialog by remember { mutableStateOf(true) }
    var characterLayout by remember { mutableStateOf(CharacterLayout.Vertical) }
    val dialogText = stringResource(id = "home_greeting_default")

    HomeAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        showCharacterDialog = showCharacterDialog,
        onShowCharacterDialogChange = { showCharacterDialog = it },
        characterLayout = characterLayout,
        onCharacterLayoutChange = { characterLayout = it },
        dialogText = dialogText
    )
}
