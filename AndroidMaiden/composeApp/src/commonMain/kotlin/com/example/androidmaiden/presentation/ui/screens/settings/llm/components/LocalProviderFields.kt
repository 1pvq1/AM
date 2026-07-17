package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource

/**
 * Fields for configuring local LLM providers, split into Local Device and LAN sections.
 */
@Composable
fun LocalProviderFields(
    address: String,
    apiKey: String,
    status: String,
    helpText: String,
    helpLink: String,
    isConnecting: Boolean,
    onAddressChange: (String) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onConnect: () -> Unit,
    onManageModels: () -> Unit
) {
    var showHelpDialog by remember { mutableStateOf(false) }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Connection Guidance") },
            text = {
                Column {
                    Text(helpText)
                    if (helpLink.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Learn more: $helpLink",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { /* TODO: Open browser */ }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) { Text("Got it") }
            }
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text(stringResource(id = "settings_advanced_llm_local_address_label")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = apiKey,
            onValueChange = onApiKeyChange,
            label = { Text("API Key (Optional)") },
            placeholder = { Text("sk-...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onConnect,
                enabled = !isConnecting,
                modifier = Modifier.weight(1f)
            ) {
                if (isConnecting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(id = "settings_advanced_llm_local_connect_button"))
                }
            }
            
            OutlinedButton(
                onClick = onManageModels,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Manage Models")
            }
        }

        if (status.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (status.contains("Successfully") || status.contains("OK")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                if (status.contains("Failed") || status.contains("Blocked") || status.contains("Refused")) {
                    IconButton(onClick = { showHelpDialog = true }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = "Show Guidance",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
