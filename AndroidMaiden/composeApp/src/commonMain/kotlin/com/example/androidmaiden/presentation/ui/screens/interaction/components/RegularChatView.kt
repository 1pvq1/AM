package com.example.androidmaiden.presentation.ui.screens.interaction.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.data.network.LlmProviderType
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.platform.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    isSending: Boolean = false,
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
            val reversedHistory = chatHistory.asReversed()
            itemsIndexed(reversedHistory) { index, message ->
                // The first item in reversed list (index 0) is the last message in chat history
                val isLastMessage = index == 0
                ChatMessageBubble(
                    chatMessage = message,
                    isStreaming = isLastMessage && isSending
                )
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