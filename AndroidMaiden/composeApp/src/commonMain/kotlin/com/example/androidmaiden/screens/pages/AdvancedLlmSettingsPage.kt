package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AdvancedLlmSettingsPagePreview() {
    AdvancedLlmSettingsPage(onNavigateBack = {})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedLlmSettingsPage(onNavigateBack: () -> Unit, viewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Advanced LLM Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
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
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text("Detailed LLM parameters and configurations will be set here.", style = MaterialTheme.typography.bodyLarge)
            // Placeholder for future settings
        }
    }
}

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
            "Connect to Local LLM",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Enter the address of a local LLM service, such as LM Studio. The application will attempt to connect and verify the endpoint.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Local Service Address") },
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
                Text("Connect")
            }
        }
        Spacer(Modifier.height(8.dp))
        if (status.isNotBlank()) {
            Text(status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
