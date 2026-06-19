package com.example.androidmaiden.presentation.ui.screens.settings.general

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.screens.*
import com.example.androidmaiden.presentation.ui.components.*import com.example.androidmaiden.presentation.viewmodel.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the Network settings group.
 */
@Preview
@Composable
fun NetworkSettingsGroupPreview() {
    NetworkSettingsContent(
        uiState = AdvancedLlmSettingsUiState(),
        onUrlChange = {},
        checkConnection = {},
        checkLocalConnection = {}
    )
}

/**
 * A group of settings related to network connectivity.
 */
@Composable
fun NetworkSettingsGroup(
    viewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NetworkSettingsContent(
        uiState = uiState,
        onUrlChange = viewModel::onOnlineCheckUrlChange,
        checkConnection = viewModel::checkOnlineConnection,
        checkLocalConnection = viewModel::checkLocalLlmConnection
    )
}

/**
 * Stateless content of the Network settings group.
 */
@Composable
fun NetworkSettingsContent(
    uiState: AdvancedLlmSettingsUiState,
    onUrlChange: (String) -> Unit,
    checkConnection: () -> Unit,
    checkLocalConnection: () -> Unit
) {
    SettingsGroup(stringResource(id = "settings_network_title")) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                "Verify your device's connectivity to both local and online resources.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            // Online Check
            OutlinedTextField(
                value = uiState.onlineCheckUrl,
                onValueChange = onUrlChange,
                label = { Text("Online Check URL") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = checkConnection,
                enabled = !uiState.isCheckingOnline,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isCheckingOnline) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Check Online Connection")
                }
            }
            if (uiState.onlineCheckStatus.isNotBlank()) {
                Text(
                    uiState.onlineCheckStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.onlineCheckStatus.contains("OK")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            
            // Local Check
            Text(
                "Local Model: ${uiState.localLlmAddress}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = checkLocalConnection,
                enabled = !uiState.isCheckingLocalLlm,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isCheckingLocalLlm) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Check Local Model Connection")
                }
            }
            if (uiState.localLlmStatus.isNotBlank()) {
                Text(
                    uiState.localLlmStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.localLlmStatus.contains("OK")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
