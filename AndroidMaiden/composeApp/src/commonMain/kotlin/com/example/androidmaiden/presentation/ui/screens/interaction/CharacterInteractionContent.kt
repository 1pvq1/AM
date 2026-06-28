package com.example.androidmaiden.presentation.ui.screens.interaction

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.local.ChatSession
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.ChatViewMode
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.screens.pages.AppBarType
import com.example.androidmaiden.presentation.ui.screens.pages.BasePage
import com.example.androidmaiden.presentation.ui.screens.interaction.components.RegularChatView
import com.example.androidmaiden.presentation.ui.screens.interaction.components.VirtualChatView
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateless UI for the Character Interaction screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInteractionContent(
    useDrawer: Boolean,
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

    val content = @Composable {
        BasePage(
            title = stringResource(id = "character_interaction"),
            appBarType = AppBarType.SMALL,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            actions = {
                if (useDrawer) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.History, contentDescription = "Chat History")
                    }
                }
                IconButton(onClick = onFullScreenToggle) {
                    Icon(
                        imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = "Full Screen"
                    )
                }
                IconButton(onClick = {
                    val newMode = if (viewMode == ChatViewMode.REGULAR) ChatViewMode.VIRTUAL else ChatViewMode.REGULAR
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

    if (useDrawer) {
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
            },
            content = content
        )
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.width(300.dp).fillMaxHeight(),
                tonalElevation = 1.dp
            ) {
                ChatHistoryDrawerContent(
                    allSessions = allSessions,
                    selectedSessionId = selectedSessionId,
                    onSessionSelect = onSessionSelect,
                    onCreateNewSession = onCreateNewSession,
                    onDeleteSession = onDeleteSession,
                    onRenameSession = onRenameSession,
                    onPinSession = onPinSession
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun CharacterInteractionPagePreview() {
    AppTheme {
        CharacterInteractionContent(
            useDrawer = true,
            isFullScreen = false,
            onFullScreenToggle = {},
            viewMode = ChatViewMode.REGULAR,
            onViewModeChange = {},
            chatHistory = emptyList<ChatMessage>(),
            text = "Hello",
            onTextChange = {},
            onSendMessage = {},
            selectedProvider = null,
            onProviderClick = {},
            showProviderPicker = false,
            availableProviders = emptyList(),
            onProviderSelect = {},
            onDismissPicker = {},
            selectedModel = null,
            availableModels = emptyList(),
            onModelSelect = {},
            showModelPicker = false,
            onModelClick = {},
            onDismissModelPicker = {},
            tokenUsage = 0.5f
        )
    }
}
