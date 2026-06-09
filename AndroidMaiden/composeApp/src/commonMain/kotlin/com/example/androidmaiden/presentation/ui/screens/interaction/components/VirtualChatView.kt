package com.example.androidmaiden.presentation.ui.screens.interaction.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.data.network.LlmProviderType
import com.example.androidmaiden.domain.model.ChatMessage
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.features.character.CharacterIllustrationBox
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable for a virtual interaction view where the character is prominent.
 */
@Composable
fun VirtualChatView(
    modifier: Modifier = Modifier,
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
    selectedModel: String?,
    availableModels: List<String>,
    onModelSelect: (String) -> Unit,
    showModelPicker: Boolean,
    onModelClick: () -> Unit,
    onDismissModelPicker: () -> Unit,
    tokenUsage: Float
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
            isSending = false,
            text = stringResource(id = "chat_preview_2"),
            onTextChange = {},
            onSendMessage = {},
            selectedProvider = LlmProvider("gemini", "Gemini", LlmProviderType.GEMINI),
            onProviderClick = {},
            showProviderPicker = false,
            availableProviders = emptyList(),
            onProviderSelect = {},
            onDismissPicker = {},
            selectedModel = "gemini-1.5-pro",
            availableModels = listOf("gemini-1.5-pro"),
            onModelSelect = {},
            showModelPicker = false,
            onModelClick = {},
            onDismissModelPicker = {},
            tokenUsage = 0.6f
        )
    }
}
