package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.views.ExpandableLlmProviderItem
import com.example.androidmaiden.presentation.ui.views.SettingsSectionHeader

/**
 * Section for local model configuration.
 */
@Composable
fun LocalModelsSection(
    localLlmAddress: String,
    localApiKey: String,
    localLlmStatus: String,
    localHelpText: String,
    localHelpLink: String,
    isCheckingLocalLlm: Boolean,
    onLocalLlmAddressChange: (String) -> Unit,
    onLocalApiKeyChange: (String) -> Unit,
    onConnect: () -> Unit,
    onManageModels: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingsSectionHeader(title = "Local Models")

        // --- Local Device Subsection ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Local Device",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Text(
                    text = "Used to download and use its small-sized models, such as 4B/E4B and below",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // --- LAN Subsection ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "LAN",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            
            ExpandableLlmProviderItem(
                name = "LM Studio",
                icon = Icons.Default.Computer,
                description = "Connect to LM Studio running on your PC."
            ) {
                LocalProviderFields(
                    address = localLlmAddress,
                    apiKey = localApiKey,
                    status = localLlmStatus,
                    helpText = localHelpText,
                    helpLink = localHelpLink,
                    isConnecting = isCheckingLocalLlm,
                    onAddressChange = onLocalLlmAddressChange,
                    onApiKeyChange = onLocalApiKeyChange,
                    onConnect = onConnect,
                    onManageModels = onManageModels
                )
            }
        }
    }
}
