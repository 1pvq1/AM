package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.viewModels.CharacterInteractionViewModel
import com.example.androidmaiden.views.character.CharacterIllustrationBox
import org.jetbrains.compose.ui.tooling.preview.Preview

// Data models for chat messages
data class ChatMessage(val message: String, val sender: Sender)
enum class Sender { USER, CHARACTER }

enum class ChatViewMode { REGULAR, VIRTUAL }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CharacterInteractionPage(
    onFullScreenChange: (Boolean) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: CharacterInteractionViewModel = viewModel { CharacterInteractionViewModel() }
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
                    onSendMessage = { viewModel.sendMessage() }
                )

                ChatViewMode.VIRTUAL -> VirtualChatView(
                    modifier = Modifier.fillMaxSize(),
                    chatHistory = viewModel.chatHistory,
                    text = viewModel.text,
                    onTextChange = { viewModel.onTextChanged(it) },
                    onSendMessage = { viewModel.sendMessage() }
                )
            }
        }
    }
}

@Composable
fun RegularChatView(
    modifier: Modifier = Modifier,
    chatHistory: List<ChatMessage>,
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit
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

        ChatInput(text = text, onTextChange = onTextChange, onSendMessage = onSendMessage)
    }
}

@Composable
fun VirtualChatView(
    modifier: Modifier = Modifier,
    chatHistory: List<ChatMessage>,
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit
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
                useTransparentStyle = true
            )
        }
    }
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    useTransparentStyle: Boolean = false
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
                Icon(Icons.Default.Send, contentDescription = stringResource(id = "send_message"))
            }
        }
    }
}

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
            onSendMessage = {}
        )
    }
}

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
            onSendMessage = {}
        )
    }
}
