package com.example.androidmaiden.presentation.ui.screens.interaction.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.network.LlmProvider
import com.example.androidmaiden.data.network.LlmProviderType
import com.example.androidmaiden.platform.stringResource

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
