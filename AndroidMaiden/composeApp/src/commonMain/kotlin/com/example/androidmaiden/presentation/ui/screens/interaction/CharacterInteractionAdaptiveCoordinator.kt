package com.example.androidmaiden.presentation.ui.screens.interaction

import androidx.compose.runtime.Composable
import com.example.androidmaiden.data.local.ChatSession
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.ChatViewMode
import com.example.androidmaiden.presentation.ui.adaptive.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Adaptive Coordinator for the Character Interaction screen.
 */
@Composable
fun CharacterInteractionAdaptiveCoordinator(
    windowSizeClass: WindowSizeClass,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
    viewMode: ChatViewMode,
    onViewModeChange: (ChatViewMode) -> Unit,
    chatHistory: List<ChatMessage>,
    isSending: Boolean,
    allSessions: List<ChatSession>,
    selectedSessionId: String?,
    onSessionSelect: (ChatSession) -> Unit,
    onCreateNewSession: () -> Unit,
    onDeleteSession: (ChatSession) -> Unit,
    onRenameSession: (ChatSession) -> Unit,
    onPinSession: (ChatSession) -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    selectedProvider: LlmProvider?,
    onProviderClick: () -> Unit,
    showProviderPicker: Boolean,
    availableProviders: List<LlmProvider>,
    onProviderSelect: (LlmProvider) -> Unit,
    onDismissPicker: () -> Unit,
    selectedModel: String?,
    availableModels: List<String>,
    onModelSelect: (String) -> Unit,
    showModelPicker: Boolean,
    onModelClick: () -> Unit,
    onDismissModelPicker: () -> Unit,
    tokenUsage: Float
) {
    // We can use windowSizeClass to decide if we want to show a permanent 
    // chat history rail on Expanded screens instead of a Drawer.
    val useDrawer = windowSizeClass.widthCategory != WindowSizeCategory.Expanded

    CharacterInteractionContent(
        useDrawer = useDrawer,
        isFullScreen = isFullScreen,
        onFullScreenToggle = onFullScreenToggle,
        viewMode = viewMode,
        onViewModeChange = onViewModeChange,
        chatHistory = chatHistory,
        isSending = isSending,
        allSessions = allSessions,
        selectedSessionId = selectedSessionId,
        onSessionSelect = onSessionSelect,
        onCreateNewSession = onCreateNewSession,
        onDeleteSession = onDeleteSession,
        onRenameSession = onRenameSession,
        onPinSession = onPinSession,
        text = text,
        onTextChange = onTextChange,
        onSendMessage = onSendMessage,
        selectedProvider = selectedProvider,
        onProviderClick = onProviderClick,
        showProviderPicker = showProviderPicker,
        availableProviders = availableProviders,
        onProviderSelect = onProviderSelect,
        onDismissPicker = onDismissPicker,
        selectedModel = selectedModel,
        availableModels = availableModels,
        onModelSelect = onModelSelect,
        showModelPicker = showModelPicker,
        onModelClick = onModelClick,
        onDismissModelPicker = onDismissModelPicker,
        tokenUsage = tokenUsage
    )
}

@Preview(name = "Compact", showBackground = true)
@Composable
fun CharacterInteractionAdaptiveCoordinatorCompactPreview() {
    AppTheme {
        CharacterInteractionAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Compact)
    }
}

@Preview(name = "Medium", showBackground = true)
@Composable
fun CharacterInteractionAdaptiveCoordinatorMediumPreview() {
    AppTheme {
        CharacterInteractionAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Medium)
    }
}

@Preview(name = "Expanded", showBackground = true)
@Composable
fun CharacterInteractionAdaptiveCoordinatorExpandedPreview() {
    AppTheme {
        CharacterInteractionAdaptiveCoordinatorPreviewHelper(WindowSizeCategory.Expanded)
    }
}

@Composable
private fun CharacterInteractionAdaptiveCoordinatorPreviewHelper(widthCategory: WindowSizeCategory) {
    CharacterInteractionAdaptiveCoordinator(
        windowSizeClass = WindowSizeClass(widthCategory, WindowSizeCategory.Medium),
        isFullScreen = false,
        onFullScreenToggle = { },
        viewMode = ChatViewMode.REGULAR,
        onViewModeChange = { },
        chatHistory = emptyList(),
        isSending = false,
        allSessions = emptyList(),
        selectedSessionId = null,
        onSessionSelect = { },
        onCreateNewSession = { },
        onDeleteSession = { },
        onRenameSession = { },
        onPinSession = { },
        text = "Preview text",
        onTextChange = { },
        onSendMessage = { },
        selectedProvider = null,
        onProviderClick = { },
        showProviderPicker = false,
        availableProviders = emptyList(),
        onProviderSelect = { },
        onDismissPicker = { },
        selectedModel = null,
        availableModels = emptyList(),
        onModelSelect = { },
        showModelPicker = false,
        onModelClick = { },
        onDismissModelPicker = { },
        tokenUsage = 0.5f
    )
}
