package com.example.androidmaiden.presentation.ui.screens.hardware

import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.viewmodel.HardwareViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * The Stateful Entry Point for the Hardware Monitor screen.
 *
 * This screen coordinates between the [HardwareViewModel] and the [HardwareContent] UI.
 * It manages the lifecycle of the hardware telemetry polling and handles user interactions
 * such as starting/stopping stress tests and navigating back to the previous screen.
 *
 * @param onBack Callback interface invoked to exit this screen and return to the Skills menu.
 * @param viewModel The ViewModel responsible for driving the state, injected via Koin.
 */
@Composable
fun HardwareScreen(
    onBack: () -> Unit,
    viewModel: HardwareViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HardwareContent(
        uiState = uiState,
        onBack = onBack,
        onStartStress = { viewModel.startStressTest(it) }
    ) { viewModel.stopStressTest() }
}
