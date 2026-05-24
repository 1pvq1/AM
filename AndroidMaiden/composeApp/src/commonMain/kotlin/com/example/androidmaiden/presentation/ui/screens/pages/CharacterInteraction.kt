package com.example.androidmaiden.presentation.ui.screens.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.network.*
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.viewmodel.CharacterInteractionViewModel
import com.example.androidmaiden.presentation.ui.features.character.CharacterIllustrationBox
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Data model for a single chat message.
 */
data class ChatMessage(val message: String, val sender: Sender)

/**
 * Enum representing the sender of a chat message.
 */
enum class Sender { USER, CHARACTER }

/**
 * Enum representing different view modes for the character interaction.
 */
enum class ChatViewMode { REGULAR, VIRTUAL }

/**
 * Main page for interacting with the AI character.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CharacterInteractionPage(
    onFullScreenChange: (Boolean) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: CharacterInteractionViewModel = koinViewModel()
) {
    // Load strings for initial chat history
    val initialChatMessages = listOf(
        ChatMessage(stringResource(id = "chat_greeting_1"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_2"), Sender.USER),
        ChatMessage(stringResource(id = "chat_greeting_3"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_4"), Sender.CHARACTER),
        ChatMessage(stringResource(id = "chat_greeting_5"), Sender.USER)
    )

    LaunchedEffect(Unit) {
        viewModel.initChat(initialChatMessages)
    }

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
            IconButton(onClick = {
                viewModel.onFullScreenToggle(onFullScreenChange)
            }) {
                Icon(
                    imageVector = if (viewModel.isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = "Full Screen"
                )
            }
            // View Mode Switch Button
            IconButton(onClick = {
                val newMode = if (viewModel.viewMode == ChatViewMode.REGULAR) ChatViewMode.VIRTUAL else ChatViewMode.REGULAR
                viewModel.onViewModeChange(newMode)
            }) {
                Icon(
                    imageVector = if (viewModel.viewMode == ChatViewMode.REGULAR) Icons.Default.Person else Icons.Default.Forum,
                    contentDescription = stringResource(id = "switch_view")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (viewModel.viewMode) {
                ChatViewMode.REGULAR -> RegularChatView(
                    modifier = Modifier.fillMaxSize(),
                    chatHistory = viewModel.chatHistory,
                    text = viewModel.text,
                    onTextChange = { viewModel.onTextChanged(it) },
                    onSendMessage = { viewModel.sendMessage() },
                    selectedProvider = viewModel.selectedProvider,
                    onProviderClick = { viewModel.toggleProviderPicker() },
                    showProviderPicker = viewModel.showProviderPicker,
                    availableProviders = viewModel.availableProviders,
                    onProviderSelect = { viewModel.onProviderSelect(it) },
                    onDismissPicker = { viewModel.toggleProviderPicker() }
                )

                ChatViewMode.VIRTUAL -> VirtualChatView(
                    modifier = Modifier.fillMaxSize(),
                    chatHistory = viewModel.chatHistory,
                    text = viewModel.text,
                    onTextChange = { viewModel.onTextChanged(it) },
                    onSendMessage = { viewModel.sendMessage() },
                    selectedProvider = viewModel.selectedProvider,
                    onProviderClick = { viewModel.toggleProviderPicker() },
                    showProviderPicker = viewModel.showProviderPicker,
                    availableProviders = viewModel.availableProviders,
                    onProviderSelect = { viewModel.onProviderSelect(it) },
                    onDismissPicker = { viewModel.toggleProviderPicker() }
                )
            }
        }
    }
}

/**
 * Composable for the standard chat interface with bubbles.
 */
@Composable
fun RegularChatView(
    modifier: Modifier = Modifier,
    chatHistory: List<ChatMessage>,
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    selectedProvider: LlmProvider?,
    onProviderClick: () -> Unit,
    showProviderPicker: Boolean,
    availableProviders: List<LlmProvider>,
    onProviderSelect: (LlmProvider) -> Unit,
    onDismissPicker: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            items(chatHistory.asReversed()) { message ->
                ChatMessageBubble(chatMessage = message)
            }
        }

        ChatInput(
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

/**
 * Composable for a virtual interaction view where the character is prominent.
 */
@Composable
fun VirtualChatView(
    modifier: Modifier = Modifier,
    chatHistory: List<ChatMessage>,
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    selectedProvider: LlmProvider?,
    onProviderClick: () -> Unit,
    showProviderPicker: Boolean,
    availableProviders: List<LlmProvider>,
    onProviderSelect: (LlmProvider) -> Unit,
    onDismissPicker: () -> Unit
) {
    val latestLlmMessage =
        chatHistory.lastOrNull { it.sender == Sender.CHARACTER }?.message ?: stringResource(id = "ellipsis")

    Box(modifier = modifier.fillMaxSize()) {
        CharacterIllustrationBox(modifier = Modifier.fillMaxSize())
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Text(
                    text = latestLlmMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            ChatInput(
                text = text,
                onTextChange = onTextChange,
                onSendMessage = onSendMessage,
                useTransparentStyle = true,
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

/**
 * Composable for the chat input field and related actions.
 */
@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    useTransparentStyle: Boolean = false,
    selectedProvider: LlmProvider? = null,
    onProviderClick: () -> Unit = {},
    showProviderPicker: Boolean = false,
    availableProviders: List<LlmProvider> = emptyList(),
    onProviderSelect: (LlmProvider) -> Unit = {},
    onDismissPicker: () -> Unit = {}
) {
    Surface(
        tonalElevation = if (useTransparentStyle) 0.dp else 3.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (useTransparentStyle) Color.Transparent else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Provider Selection Button
            Box {
                IconButton(onClick = onProviderClick) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Select Provider",
                        tint = if (selectedProvider?.type == LlmProviderType.LOCAL_LM_STUDIO) 
                            MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                    )
                }
                
                DropdownMenu(
                    expanded = showProviderPicker,
                    onDismissRequest = onDismissPicker
                ) {
                    availableProviders.forEach { provider ->
                        DropdownMenuItem(
                            text = { Text(provider.name) },
                            onClick = { onProviderSelect(provider) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = if (provider.type == LlmProviderType.LOCAL_LM_STUDIO) 
                                        MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text(stringResource(id = "message_input_label")) },
                modifier = Modifier.weight(1f),
                colors = if (useTransparentStyle) {
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                } else {
                    TextFieldDefaults.colors()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSendMessage,
                enabled = text.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(id = "send_message"))
            }
        }
    }
}

/**
 * Composable for a single message bubble in the chat list.
 */
@Composable
fun ChatMessageBubble(chatMessage: ChatMessage) {
    val isUserMessage = chatMessage.sender == Sender.USER
    val bubbleColor =
        if (isUserMessage) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val avatarName = if (isUserMessage) stringResource(id = "user_avatar_name") else stringResource(id = "character_avatar_name")
    val avatarBackgroundColor =
        if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isUserMessage) {
            AvatarWithName(name = avatarName, avatarColor = avatarBackgroundColor)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            modifier = Modifier.weight(1f, fill = false),
            shape = MaterialTheme.shapes.medium,
            color = bubbleColor,
            tonalElevation = 1.dp
        ) {
            Text(
                text = chatMessage.message,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        if (isUserMessage) {
            Spacer(modifier = Modifier.width(8.dp))
            AvatarWithName(name = avatarName, avatarColor = avatarBackgroundColor)
        }
    }
}

/**
 * Composable for displaying an avatar with a name label.
 */
@Composable
fun AvatarWithName(name: String, avatarColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = avatarColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = name.take(2),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, style = MaterialTheme.typography.labelSmall)
    }
}

/**
 * Preview for the standard chat view.
 */
@Preview
@Composable
fun RegularChatViewPreview() {
    MaterialTheme {
        RegularChatView(
            chatHistory = listOf(
                ChatMessage(stringResource(id = "chat_greeting_1"), Sender.CHARACTER),
                ChatMessage(stringResource(id = "chat_greeting_2"), Sender.USER)
            ),
            text = stringResource(id = "chat_preview_1"),
            onTextChange = {},
            onSendMessage = {},
            selectedProvider = LlmProvider("gemini", "Gemini", LlmProviderType.GEMINI),
            onProviderClick = {},
            showProviderPicker = false,
            availableProviders = emptyList(),
            onProviderSelect = {},
            onDismissPicker = {}
        )
    }
}

/**
 * Preview for the virtual interaction chat view.
 */
@Preview
@Composable
fun VirtualChatViewPreview() {
    MaterialTheme {
        VirtualChatView(
            chatHistory = listOf(
                ChatMessage(stringResource(id = "chat_greeting_3"), Sender.CHARACTER),
                ChatMessage(stringResource(id = "chat_greeting_5"), Sender.USER)
            ),
            text = stringResource(id = "chat_preview_2"),
            onTextChange = {},
            onSendMessage = {},
            selectedProvider = LlmProvider("gemini", "Gemini", LlmProviderType.GEMINI),
            onProviderClick = {},
            showProviderPicker = false,
            availableProviders = emptyList(),
            onProviderSelect = {},
            onDismissPicker = {}
        )
    }
}
