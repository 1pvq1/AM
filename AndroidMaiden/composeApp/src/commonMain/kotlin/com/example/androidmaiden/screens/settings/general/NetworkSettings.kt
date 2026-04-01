package com.example.androidmaiden.screens.settings.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.screens.SettingsGroup
import com.example.androidmaiden.viewModels.rememberAdvancedLlmSettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

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
