package com.example.androidmaiden.presentation.ui.screens.interaction

import androidx.compose.runtime.*
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.viewmodel.CharacterInteractionViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * The Stateful Entry Point for the Character Interaction screen.
 */
@Composable
fun CharacterInteractionScreen(
    onFullScreenChange: (Boolean) -> Unit = {},
    viewModel: CharacterInteractionViewModel = koinViewModel()
) {
    // Load strings for initial chat history if needed
    val initialChatMessages = initSampleMessages()

    LaunchedEffect(Unit) {
        viewModel.initChat(initialChatMessages)
    }

    val windowSizeClass = LocalWindowSizeClass.current

    CharacterInteractionAdaptiveCoordinator(
        windowSizeClass = windowSizeClass,
        isFullScreen = viewModel.isFullScreen,
        onFullScreenToggle = { viewModel.onFullScreenToggle(onFullScreenChange) },
        viewMode = viewModel.viewMode,
        onViewModeChange = { viewModel.onViewModeChange(it) },
        chatHistory = viewModel.chatHistory,
        isSending = viewModel.isSending,
        allSessions = viewModel.allSessions,
        selectedSessionId = viewModel.currentSessionId,
        onSessionSelect = { viewModel.onSessionSelect(it) },
        onCreateNewSession = { viewModel.createNewSession() },
        onDeleteSession = { viewModel.deleteSession(it) },
        onRenameSession = { viewModel.renameSession(it, "New Name") },
        onPinSession = { viewModel.togglePinSession(it) },
        text = viewModel.text,
        onTextChange = { viewModel.onTextChanged(it) },
        onSendMessage = { viewModel.sendMessage() },
        selectedProvider = viewModel.selectedProvider,
        onProviderClick = { viewModel.toggleProviderPicker() },
        showProviderPicker = viewModel.showProviderPicker,
        availableProviders = viewModel.availableProviders,
        onProviderSelect = { viewModel.onProviderSelect(it) },
        onDismissPicker = { viewModel.toggleProviderPicker() },
        selectedModel = viewModel.selectedModel,
        availableModels = viewModel.availableModels,
        onModelSelect = { viewModel.onModelSelect(it) },
        showModelPicker = viewModel.showModelPicker,
        onModelClick = { viewModel.toggleModelPicker() },
        onDismissModelPicker = { viewModel.toggleModelPicker() },
        tokenUsage = viewModel.tokenUsage
    )
}

@Composable
private fun initSampleMessages(): List<ChatMessage> {
    return listOf(
        ChatMessage(stringResource(id = "chat_greeting_1"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_2"), Sender.USER),
        ChatMessage(stringResource(id = "chat_greeting_3"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_4"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_5"), Sender.USER)
    )
}
