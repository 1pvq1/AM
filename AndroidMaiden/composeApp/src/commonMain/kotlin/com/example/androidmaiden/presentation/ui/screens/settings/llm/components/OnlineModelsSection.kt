package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.views.ExpandableLlmProviderItem
import com.example.androidmaiden.presentation.ui.views.OnlineProviderFields
import com.example.androidmaiden.presentation.ui.views.SettingsSectionHeader

/**
 * Section for online model configuration (Gemini, OpenAI, Custom).
 */
@Composable
fun OnlineModelsSection(
    geminiApiKey: String,
    onGeminiKeyChange: (String) -> Unit,
    openaiApiKey: String,
    onOpenAiKeyChange: (String) -> Unit,
    customProviderUrl: String,
    onCustomUrlChange: (String) -> Unit,
    customProviderApiKey: String,
    onCustomKeyChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingsSectionHeader(title = "Online Models")

        ExpandableLlmProviderItem(
            name = "Google Gemini",
            icon = Icons.Default.Cloud,
            description = "Official Gemini API integration."
        ) {
            OnlineProviderFields(
                apiKey = geminiApiKey,
                onKeyChange = onGeminiKeyChange,
                apiKeyLabel = "Gemini API Key",
                placeholder = "Enter your API key from AI Studio"
            )
        }

        ExpandableLlmProviderItem(
            name = "OpenAI",
            icon = Icons.Default.AutoAwesome,
            description = "Connect to GPT-4, GPT-3.5, etc."
        ) {
            OnlineProviderFields(
                apiKey = openaiApiKey,
                onKeyChange = onOpenAiKeyChange,
                apiKeyLabel = "OpenAI API Key",
                placeholder = "sk-..."
            )
        }
        
        ExpandableLlmProviderItem(
            name = "Custom Provider",
            icon = Icons.Default.Extension,
            description = "OpenAI-compatible custom endpoint."
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = customProviderUrl,
                    onValueChange = onCustomUrlChange,
                    label = { Text("Base URL") },
                    placeholder = { Text("https://api.provider.com/v1") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OnlineProviderFields(
                    apiKey = customProviderApiKey,
                    onKeyChange = onCustomKeyChange,
                    apiKeyLabel = "API Key",
                    placeholder = "Optional"
                )
            }
        }
    }
}
