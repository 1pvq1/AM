package com.example.androidmaiden.presentation.ui.screens.interaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.local.ChatSession
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
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
    // Load strings for initial chat history if needed
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
        allSessions = viewModel.allSessions,
        selectedSessionId = viewModel.currentSessionId,
        onSessionSelect = { viewModel.onSessionSelect(it) },
        onCreateNewSession = { viewModel.createNewSession() },
        onDeleteSession = { viewModel.deleteSession(it) },
        onRenameSession = { viewModel.renameSession(it, "New Name") }, // TODO: Add rename dialog
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
        tokenUsage = viewModel.tokenUsage,
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
    allSessions: List<ChatSession> = emptyList(),
    selectedSessionId: String? = null,
    onSessionSelect: (ChatSession) -> Unit = {},
    onCreateNewSession: () -> Unit = {},
    onDeleteSession: (ChatSession) -> Unit = {},
    onRenameSession: (ChatSession) -> Unit = {},
    onPinSession: (ChatSession) -> Unit = {},
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
    tokenUsage: Float,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ChatHistoryDrawerContent(
                    allSessions = allSessions,
                    selectedSessionId = selectedSessionId,
                    onSessionSelect = { 
                        onSessionSelect(it)
                        scope.launch { drawerState.close() }
                    },
                    onCreateNewSession = {
                        onCreateNewSession()
                        scope.launch { drawerState.close() }
                    },
                    onDeleteSession = onDeleteSession,
                    onRenameSession = onRenameSession,
                    onPinSession = onPinSession
                )
            }
        }
    ) {
        BasePage(
            title = stringResource(id = "character_interaction"),
            appBarType = AppBarType.SMALL,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            actions = {
                // Chat History Button
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
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
                        onDismissPicker = onDismissPicker,
                        selectedModel = selectedModel,
                        availableModels = availableModels,
                        onModelSelect = onModelSelect,
                        showModelPicker = showModelPicker,
                        onModelClick = onModelClick,
                        onDismissModelPicker = onDismissModelPicker,
                        tokenUsage = tokenUsage
                    )

                    ChatViewMode.VIRTUAL -> VirtualChatView(
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
            allSessions = emptyList(),
            selectedSessionId = null,
            onSessionSelect = {},
            onCreateNewSession = {},
            onDeleteSession = {},
            onRenameSession = {},
            onPinSession = {},
            text = "Hello",
            onTextChange = {},
            onSendMessage = {},
            selectedProvider = sampleProviders[0],
            onProviderClick = {},
            showProviderPicker = false,
            availableProviders = sampleProviders,
            onProviderSelect = {},
            onDismissPicker = {},
            selectedModel = "gemini-1.5-pro",
            availableModels = listOf("gemini-1.5-pro"),
            onModelSelect = {},
            showModelPicker = false,
            onModelClick = {},
            onDismissModelPicker = {},
            tokenUsage = 0.5f
        )
    }
}
