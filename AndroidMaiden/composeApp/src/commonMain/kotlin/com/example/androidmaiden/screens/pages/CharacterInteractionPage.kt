package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.views.character.CharacterIllustrationBox
import org.jetbrains.compose.ui.tooling.preview.Preview

// Data models for chat messages
data class ChatMessage(val message: String, val sender: Sender)
enum class Sender { USER, CHARACTER }

enum class ChatViewMode { REGULAR, VIRTUAL }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CharacterInteractionPage() {
    var viewMode by remember { mutableStateOf(ChatViewMode.REGULAR) }
    val chatHistory = remember {
        mutableStateListOf(
            ChatMessage("Hello!", Sender.CHARACTER),
            ChatMessage("How are you?", Sender.USER),
            ChatMessage("This is a placeholder for our conversation.", Sender.CHARACTER),
            ChatMessage("I'm looking forward to our chat!", Sender.CHARACTER),
            ChatMessage("Me too! What should we talk about?", Sender.USER)
        )
    }
    var text by remember { mutableStateOf("") }

    val onSendMessage = {
        if (text.isNotBlank()) {
            chatHistory.add(ChatMessage(text, Sender.USER))
            text = ""
            // TODO: Implement character response logic
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Interaction") },
                actions = {
                    IconButton(onClick = {
                        viewMode = if (viewMode == ChatViewMode.REGULAR) ChatViewMode.VIRTUAL else ChatViewMode.REGULAR
                    }) {
                        Icon(
                            imageVector = if (viewMode == ChatViewMode.REGULAR) Icons.Default.Person else Icons.Default.Forum,
                            contentDescription = "Switch View"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            )
        }
    ) { paddingValues ->
        when (viewMode) {
            ChatViewMode.REGULAR -> RegularChatView(
                modifier = Modifier.padding(paddingValues),
                chatHistory = chatHistory,
                text = text,
                onTextChange = { text = it },
                onSendMessage = onSendMessage
            )
            ChatViewMode.VIRTUAL -> VirtualChatView(
                modifier = Modifier.padding(paddingValues),
                chatHistory = chatHistory,
                text = text,
                onTextChange = { text = it },
                onSendMessage = onSendMessage
            )
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
    val latestLlmMessage = chatHistory.lastOrNull { it.sender == Sender.CHARACTER }?.message ?: "..."

    Box(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text("Message...") },
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
                Icon(Icons.Default.Send, contentDescription = "Send Message")
            }
        }
    }
}

@Composable
fun ChatMessageBubble(chatMessage: ChatMessage) {
    val isUserMessage = chatMessage.sender == Sender.USER
    val bubbleColor = if (isUserMessage) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val avatarName = if (isUserMessage) "Me" else "AM"
    val avatarBackgroundColor = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

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
                ChatMessage("Hello!", Sender.CHARACTER),
                ChatMessage("How are you?", Sender.USER)
            ),
            text = "Hi there!",
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
                ChatMessage("This is a placeholder for our conversation.", Sender.CHARACTER),
                ChatMessage("Me too! What should we talk about?", Sender.USER)
            ),
            text = "Let's talk about something.",
            onTextChange = {},
            onSendMessage = {}
        )
    }
}
