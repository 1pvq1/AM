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
 * A group of settings related to network connectivity.
 */
@Preview
@Composable
fun NetworkSettingsGroup() {
    val viewModel = rememberAdvancedLlmSettingsViewModel()
    val uiState by viewModel.uiState.collectAsState()

    SettingsGroup(stringResource(id = "settings_network_title")) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                stringResource(id = "settings_network_description"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = uiState.websiteUrl,
                onValueChange = viewModel::onWebsiteUrlChange,
                label = { Text(stringResource(id = "settings_network_website_url_label")) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = viewModel::checkWebsiteConnectivity,
                enabled = !uiState.isCheckingWebsite,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isCheckingWebsite) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(stringResource(id = "settings_network_check_connectivity_button"))
                }
            }
            Spacer(Modifier.height(8.dp))
            if (uiState.websiteStatus.isNotBlank()) {
                Text(
                    uiState.websiteStatus,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}
