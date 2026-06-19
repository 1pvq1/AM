package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.screens.pages.AppBarType
import com.example.androidmaiden.presentation.ui.screens.pages.BasePage
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsViewModel
import com.example.androidmaiden.presentation.viewmodel.rememberAdvancedLlmSettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the Advanced LLM Settings page.
 */
@Preview
@Composable
fun AdvancedLlmSettingsPagePreview() {
    AdvancedLlmSettingsContent(
        uiState = AdvancedLlmSettingsUiState(),
        onNavigateBack = {},
        onLocalLlmAddressChange = {},
        onLocalApiKeyChange = {},
        onConnect = {},
        onGeminiKeyChange = {},
        onOpenAiKeyChange = {},
        onCustomUrlChange = {},
        onCustomKeyChange = {},
        onToggleModelEnabled = {},
        onUpdateModelOrder = { _, _ -> }
    )
}

/**
 * Screen for configuring advanced LLM settings.
 */
@Composable
fun AdvancedLlmSettingsPage(
    onNavigateBack: () -> Unit,
    viewModel: AdvancedLlmSettingsViewModel = rememberAdvancedLlmSettingsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AdvancedLlmSettingsContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onLocalLlmAddressChange = viewModel::onLocalLlmAddressChange,
        onLocalApiKeyChange = viewModel::onLocalApiKeyChange,
        onConnect = viewModel::checkLocalLlmConnection,
        onGeminiKeyChange = viewModel::onGeminiApiKeyChange,
        onOpenAiKeyChange = viewModel::onOpenAiApiKeyChange,
        onCustomUrlChange = viewModel::onCustomProviderUrlChange,
        onCustomKeyChange = viewModel::onCustomProviderApiKeyChange,
        onToggleModelEnabled = viewModel::toggleModelEnabled,
        onUpdateModelOrder = viewModel::updateModelOrder
    )
}

/**
 * Content of the Advanced LLM Settings page.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedLlmSettingsContent(
    uiState: AdvancedLlmSettingsUiState,
    onNavigateBack: () -> Unit,
    onLocalLlmAddressChange: (String) -> Unit,
    onLocalApiKeyChange: (String) -> Unit,
    onConnect: () -> Unit,
    onGeminiKeyChange: (String) -> Unit,
    onOpenAiKeyChange: (String) -> Unit,
    onCustomUrlChange: (String) -> Unit,
    onCustomKeyChange: (String) -> Unit,
    onToggleModelEnabled: (String) -> Unit,
    onUpdateModelOrder: (Int, Int) -> Unit
) {
    var showModelsList by remember { mutableStateOf(false) }

    if (showModelsList) {
        ModelsListDialog(
            models = uiState.discoveredModels,
            onDismiss = { showModelsList = false },
            onToggleEnabled = onToggleModelEnabled,
            onMove = onUpdateModelOrder
        )
    }

    BasePage(
        title = stringResource(id = "settings_advanced_llm_title"),
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = onNavigateBack,
        appBarType = AppBarType.SMALL,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = "settings_advanced_llm_description"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // --- Local Models Section ---
            item {
                SettingsSectionHeader(title = "Local Models")
            }
            
            item {
                ExpandableLlmProviderItem(
                    name = "LM Studio",
                    icon = Icons.Default.Computer,
                    description = "Connect to LM Studio running on your PC."
                ) {
                    LocalProviderFields(
                        address = uiState.localLlmAddress,
                        apiKey = uiState.localApiKey,
                        status = uiState.localLlmStatus,
                        helpText = uiState.localHelpText,
                        helpLink = uiState.localHelpLink,
                        isConnecting = uiState.isCheckingLocalLlm,
                        onAddressChange = onLocalLlmAddressChange,
                        onApiKeyChange = onLocalApiKeyChange,
                        onConnect = onConnect,
                        onManageModels = { showModelsList = true }
                    )
                }
            }

            // --- Online Models Section ---
            item {
                SettingsSectionHeader(title = "Online Models")
            }

            item {
                ExpandableLlmProviderItem(
                    name = "Google Gemini",
                    icon = Icons.Default.Cloud,
                    description = "Official Gemini API integration."
                ) {
                    OnlineProviderFields(
                        apiKey = uiState.geminiApiKey,
                        onKeyChange = onGeminiKeyChange,
                        apiKeyLabel = "Gemini API Key",
                        placeholder = "Enter your API key from AI Studio"
                    )
                }
            }

            item {
                ExpandableLlmProviderItem(
                    name = "OpenAI",
                    icon = Icons.Default.AutoAwesome,
                    description = "Connect to GPT-4, GPT-3.5, etc."
                ) {
                    OnlineProviderFields(
                        apiKey = uiState.openaiApiKey,
                        onKeyChange = onOpenAiKeyChange,
                        apiKeyLabel = "OpenAI API Key",
                        placeholder = "sk-..."
                    )
                }
            }
            
            item {
                ExpandableLlmProviderItem(
                    name = "Custom Provider",
                    icon = Icons.Default.Extension,
                    description = "OpenAI-compatible custom endpoint."
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.customProviderUrl,
                            onValueChange = onCustomUrlChange,
                            label = { Text("Base URL") },
                            placeholder = { Text("https://api.provider.com/v1") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OnlineProviderFields(
                            apiKey = uiState.customProviderApiKey,
                            onKeyChange = onCustomKeyChange,
                            apiKeyLabel = "API Key",
                            placeholder = "Optional"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

/**
 * Reusable expandable item for LLM providers.
 */
@Composable
private fun ExpandableLlmProviderItem(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun LocalProviderFields(
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

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

@Composable
private fun OnlineProviderFields(
    apiKey: String,
    onKeyChange: (String) -> Unit,
    apiKeyLabel: String,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = apiKey,
            onValueChange = onKeyChange,
            label = { Text(apiKeyLabel) },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        )
        
        Text(
            text = "API keys are stored securely on your device.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
