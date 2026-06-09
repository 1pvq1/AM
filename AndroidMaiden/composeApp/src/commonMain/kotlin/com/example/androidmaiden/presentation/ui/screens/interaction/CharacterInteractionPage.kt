package com.example.androidmaiden.presentation.ui.screens.interaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.data.network.LlmProviderType
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.ChatViewMode
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.screens.pages.AppBarType
import com.example.androidmaiden.presentation.ui.screens.pages.BasePage
import com.example.androidmaiden.presentation.ui.screens.interaction.components.RegularChatView
import com.example.androidmaiden.presentation.ui.screens.interaction.components.VirtualChatView
import com.example.androidmaiden.presentation.viewmodel.CharacterInteractionViewModel
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Main page for interacting with the AI character.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInteractionPage(
    onFullScreenChange: (Boolean) -> Unit = {},
    viewModel: CharacterInteractionViewModel = koinViewModel()
) {
    // Load strings for initial chat history
    val initialChatMessages = initSampleMessages()

    LaunchedEffect(Unit) {
        viewModel.initChat(initialChatMessages)
    }

    CharacterInteractionContent(
        isFullScreen = viewModel.isFullScreen,
        onFullScreenToggle = { viewModel.onFullScreenToggle(onFullScreenChange) },
        viewMode = viewModel.viewMode,
        onViewModeChange = { viewModel.onViewModeChange(it) },
        chatHistory = viewModel.chatHistory,
        isSending = viewModel.isSending,
        text = viewModel.text,
        onTextChange = { viewModel.onTextChanged(it) },
        onSendMessage = { viewModel.sendMessage() },
        selectedProvider = viewModel.selectedProvider,
        onProviderClick = { viewModel.toggleProviderPicker() },
        showProviderPicker = viewModel.showProviderPicker,
        availableProviders = viewModel.availableProviders,
        onProviderSelect = { viewModel.onProviderSelect(it) },
        onDismissPicker = { viewModel.toggleProviderPicker() },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun initSampleMessages(): List<ChatMessage> {
    val initialChatMessages = listOf(
        ChatMessage(stringResource(id = "chat_greeting_1"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_2"), Sender.USER),
        ChatMessage(stringResource(id = "chat_greeting_3"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_4"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_5"), Sender.USER)
    )
    return initialChatMessages
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterInteractionContent(
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
    viewMode: ChatViewMode,
    onViewModeChange: (ChatViewMode) -> Unit,
    chatHistory: List<ChatMessage>,
    isSending: Boolean = false,
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    selectedProvider: LlmProvider?,
    onProviderClick: () -> Unit,
    showProviderPicker: Boolean,
    availableProviders: List<LlmProvider>,
    onProviderSelect: (LlmProvider) -> Unit,
    onDismissPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasePage(
        title = stringResource(id = "character_interaction"),
        appBarType = AppBarType.SMALL,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        actions = {
            // Chat History Button
            IconButton(onClick = { /* TODO: Show chat history */ }) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Chat History"
                )
            }
            // Full Screen Button
            IconButton(onClick = onFullScreenToggle) {
                Icon(
                    imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = "Full Screen"
                )
            }
            // View Mode Switch Button
            IconButton(onClick = {
                val newMode =
                    if (viewMode == ChatViewMode.REGULAR) ChatViewMode.VIRTUAL else ChatViewMode.REGULAR
                onViewModeChange(newMode)
            }) {
                Icon(
                    imageVector = if (viewMode == ChatViewMode.REGULAR) Icons.Default.Person else Icons.Default.Forum,
                    contentDescription = stringResource(id = "switch_view")
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (viewMode) {
                ChatViewMode.REGULAR -> RegularChatView(
                    modifier = Modifier.fillMaxSize(),
                    chatHistory = chatHistory,
                    isSending = isSending,
                    text = text,
                    onTextChange = onTextChange,
                    onSendMessage = onSendMessage,
                    selectedProvider = selectedProvider,
                    onProviderClick = onProviderClick,
                    showProviderPicker = showProviderPicker,
                    availableProviders = availableProviders,
                    onProviderSelect = onProviderSelect,
                    onDismissPicker = onDismissPicker
                )

                ChatViewMode.VIRTUAL -> VirtualChatView(
                    modifier = Modifier.fillMaxSize(),
                    chatHistory = chatHistory,
                    text = text,
                    onTextChange = onTextChange,
                    onSendMessage = onSendMessage,
                    selectedProvider = selectedProvider,
                    onProviderClick = onProviderClick,
                    showProviderPicker = showProviderPicker,
                    availableProviders = availableProviders,
                    onProviderSelect = onProviderSelect,
                    onDismissPicker = onDismissPicker
                )
            }
        }
    }
}

@Preview
@Composable
fun CharacterInteractionPagePreview() {
    val sampleChatHistory = listOf(
        ChatMessage("Hello! How can I help you today?", Sender.CHARACTER),
        ChatMessage("I want to learn about Jetpack Compose Previews.", Sender.USER),
        ChatMessage(
            "Previews are a great way to visualize your UI without running the app.",
            Sender.CHARACTER
        )

    ) + initSampleMessages()
    val sampleProviders = listOf(
        LlmProvider("gemini", "Gemini (Online)", LlmProviderType.GEMINI),
        LlmProvider("local", "LM Studio (Local)", LlmProviderType.LOCAL_LM_STUDIO)
    )

    AppTheme {
        CharacterInteractionContent(
            isFullScreen = false,
            onFullScreenToggle = {},
            viewMode = ChatViewMode.REGULAR,
            onViewModeChange = {},
            chatHistory = sampleChatHistory,
            isSending = false,
            text = "Hello",
            onTextChange = {},
            onSendMessage = {},
            selectedProvider = sampleProviders[0],
            onProviderClick = {},
            showProviderPicker = false,
            availableProviders = sampleProviders,
            onProviderSelect = {},
            onDismissPicker = {}
        )
    }
}
