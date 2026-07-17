package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsViewModel
import com.example.androidmaiden.presentation.viewmodel.rememberAdvancedLlmSettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the Advanced LLM Settings page.
 */
@Preview
@Composable
fun AdvancedLlmSettingsPagePreview() {
    AdvancedLlmSettingsContent(
        uiState = AdvancedLlmSettingsUiState(),
        onNavigateBack = {},
        onLocalLlmAddressChange = {},
        onLocalApiKeyChange = {},
        onConnect = {},
        onGeminiKeyChange = {},
        onOpenAiKeyChange = {},
        onCustomUrlChange = {},
        onCustomKeyChange = {},
        onToggleModelEnabled = {},
        onUpdateModelOrder = { _, _ -> }
    )
}

/**
 * Stateful screen for configuring advanced LLM settings.
 */
@Composable
fun AdvancedLlmSettingsPage(
    onNavigateBack: () -> Unit,
    viewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AdvancedLlmSettingsContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onLocalLlmAddressChange = viewModel::onLocalLlmAddressChange,
        onLocalApiKeyChange = viewModel::onLocalApiKeyChange,
        onConnect = viewModel::checkLocalLlmConnection,
        onGeminiKeyChange = viewModel::onGeminiApiKeyChange,
        onOpenAiKeyChange = viewModel::onOpenAiApiKeyChange,
        onCustomUrlChange = viewModel::onCustomProviderUrlChange,
        onCustomKeyChange = viewModel::onCustomProviderApiKeyChange,
        onToggleModelEnabled = viewModel::toggleModelEnabled,
        onUpdateModelOrder = viewModel::updateModelOrder
    )
}
