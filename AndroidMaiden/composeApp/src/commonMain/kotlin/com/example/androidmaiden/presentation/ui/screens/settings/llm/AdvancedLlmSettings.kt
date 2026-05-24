package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.screens.pages.*
import com.example.androidmaiden.presentation.ui.components.*import com.example.androidmaiden.presentation.viewmodel.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the Advanced LLM Settings page.
 */
@Preview
@Composable
fun AdvancedLlmSettingsPagePreview() {
    AdvancedLlmSettingsPage(onNavigateBack = {})
}


/**
 * Screen for configuring advanced LLM settings like local addresses.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedLlmSettingsPage(
    onNavigateBack: () -> Unit,
    viewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BasePage(
        title = stringResource(id = "settings_advanced_llm_title"),
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = onNavigateBack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LocalLlmConnectionSection(
                address = uiState.localLlmAddress,
                status = uiState.localLlmStatus,
                isConnecting = uiState.isConnectingToLocalLlm,
                onAddressChange = viewModel::onLocalLlmAddressChange,
                onConnect = viewModel::connectToLocalLlm
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Text(
                stringResource(id = "settings_advanced_llm_description"),
                style = MaterialTheme.typography.bodyLarge
            )
            // Placeholder for future settings
        }

    }
}

/**
 * Section for configuring local LLM connections.
 */
@Composable
private fun LocalLlmConnectionSection(
    address: String,
    status: String,
    isConnecting: Boolean,
    onAddressChange: (String) -> Unit,
    onConnect: () -> Unit
) {
    Column {
        Text(
            stringResource(id = "settings_advanced_llm_local_title"),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(id = "settings_advanced_llm_local_description"),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text(stringResource(id = "settings_advanced_llm_local_address_label")) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onConnect,
            enabled = !isConnecting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isConnecting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(id = "settings_advanced_llm_local_connect_button"))
            }
        }
        Spacer(Modifier.height(8.dp))
        if (status.isNotBlank()) {
            Text(status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
