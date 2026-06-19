package com.example.androidmaiden.presentation.ui.screens.interaction.components

import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.data.network.LlmProviderType
import com.example.androidmaiden.platform.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    onDismissPicker: () -> Unit = {},
    selectedModel: String? = null,
    availableModels: List<String> = emptyList(),
    onModelSelect: (String) -> Unit = {},
    showModelPicker: Boolean = false,
    onModelClick: () -> Unit = {},
    onDismissModelPicker: () -> Unit = {},
    tokenUsage: Float = 0f
) {
    Surface(
        tonalElevation = if (useTransparentStyle) 0.dp else 3.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (useTransparentStyle) Color.Transparent else MaterialTheme.colorScheme.surface
    ) {
        Column {
            MsgInputArea(text, onTextChange, useTransparentStyle, onSendMessage)

            ModelConfigBar(
                onProviderClick,
                selectedProvider,
                showProviderPicker,
                onDismissPicker,
                availableProviders,
                onProviderSelect,
                onModelClick,
                selectedModel,
                showModelPicker,
                onDismissModelPicker,
                availableModels,
                onModelSelect,
                tokenUsage
            )
        }
    }
}

@Composable
private fun ModelConfigBar(
    onProviderClick: () -> Unit,
    selectedProvider: LlmProvider?,
    showProviderPicker: Boolean,
    onDismissPicker: () -> Unit,
    availableProviders: List<LlmProvider>,
    onProviderSelect: (LlmProvider) -> Unit,
    onModelClick: () -> Unit,
    selectedModel: String?,
    showModelPicker: Boolean,
    onDismissModelPicker: () -> Unit,
    availableModels: List<String>,
    onModelSelect: (String) -> Unit,
    tokenUsage: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Provider Selection Button
        ModelsProvider(
            onProviderClick,
            selectedProvider,
            showProviderPicker,
            onDismissPicker,
            availableProviders,
            onProviderSelect
        )

        // Model Selector Button
        Box {
            TextButton(onClick = onModelClick) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = selectedModel ?: "Select Model",
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.width(80.dp)
                )
            }
            DropdownMenu(
                expanded = showModelPicker,
                onDismissRequest = onDismissModelPicker
            ) {
                availableModels.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(model) },
                        onClick = { onModelSelect(model) }
                    )
                }
            }
        }

        if (selectedProvider?.type == LlmProviderType.LOCAL_LM_STUDIO) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Local Host Connected",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(Modifier.size(12.dp))

        // Token Usage Progress Ring
        TokenUsageRing(tokenUsage)
    }
}

@Composable
private fun MsgInputArea(
    text: String,
    onTextChange: (String) -> Unit,
    useTransparentStyle: Boolean,
    onSendMessage: () -> Unit
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

        IconButton(
            onClick = onSendMessage,
            enabled = text.isNotBlank()
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(id = "send_message")
            )
        }
    }
}

@Composable
private fun ModelsProvider(
    onProviderClick: () -> Unit,
    selectedProvider: LlmProvider?,
    showProviderPicker: Boolean,
    onDismissPicker: () -> Unit,
    availableProviders: List<LlmProvider>,
    onProviderSelect: (LlmProvider) -> Unit
) {
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
}

@Composable
private fun TokenUsageRing(tokenUsage: Float) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .clickable { /* Toggle usage details */ },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { tokenUsage },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 3.dp,
            color = if (tokenUsage > 0.8f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = "${(tokenUsage * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
        )
    }
}

@Preview
@Composable
fun PreviewChatInput() {
    ChatInput(
        text = "Hello!",
        onTextChange = {},
        onSendMessage = {},
        onProviderClick = { },
        onProviderSelect = { },
        onDismissPicker = {}
    )
}