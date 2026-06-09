package com.example.androidmaiden.presentation.ui.screens.interaction.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.markdown.MarkdownRenderer
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable for a single message bubble in the chat list.
 */
@Composable
fun ChatMessageBubble(chatMessage: ChatMessage, isStreaming: Boolean = false) {
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
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                MarkdownRenderer(
                    content = chatMessage.message,
                    isStreaming = isStreaming && !isUserMessage
                )
            }
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

@Preview
@Composable
fun ChatMessageBubblePreview() {
    val previewMessage = ChatMessage(
        message = "Hello! How can I help you today?",
        sender = Sender.CHARACTER
    )
    ChatMessageBubble(chatMessage = previewMessage)

}

@Preview
@Composable
fun AvatarWithNamePreview() {
    AvatarWithName(name = "John Doe", avatarColor = Color.Unspecified)
}