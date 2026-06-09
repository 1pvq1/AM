package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.data.repository.SettingsRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

/**
 * Preview for the LLM settings Content.
 */
@Preview
@Composable
fun PreviewLlmSettingsContent() {
    LlmSettingsContent(
        apiKey = "preview-api-key",
        onApiKeyChange = {},
        localLlmAddress = "http://localhost:1234/v1",
        onLocalLlmAddressChange = {},
        useMatureMarkdown = true,
        onMatureMarkdownToggle = {},
        onNavigateToAdvancedLlmSettings = {}
    )
}

@Preview
@Composable
fun PreviewSettingsGroup() {
    // Use dummy values for preview
    LlmSettingsContent(
        apiKey = "",
        onApiKeyChange = {},
        localLlmAddress = "",
        onLocalLlmAddressChange = {},
        useMatureMarkdown = true,
        onMatureMarkdownToggle = {},
        onNavigateToAdvancedLlmSettings = {}
    )
}

/**
 * A group of settings related to Language Learning Models (LLMs).
 */
@Composable
fun LlmSettingsGroup(onNavigateToAdvancedLlmSettings: () -> Unit) {
    val settingsRepository = koinInject<SettingsRepository>()
    var apiKey by remember { mutableStateOf("") }
    var localLlmAddress by remember { mutableStateOf("") }
    var useMatureMarkdown by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        apiKey = settingsRepository.apiKey.first() ?: ""
        localLlmAddress = settingsRepository.localLlmAddress.first()
        useMatureMarkdown = settingsRepository.useMatureMarkdown.first()
    }

    LlmSettingsContent(
        apiKey = apiKey,
        onApiKeyChange = {
            apiKey = it
            scope.launch {
                settingsRepository.saveApiKey(it)
            }
        },
        localLlmAddress = localLlmAddress,
        onLocalLlmAddressChange = {
            localLlmAddress = it
            scope.launch {
                settingsRepository.saveLocalLlmAddress(it)
            }
        },
        useMatureMarkdown = useMatureMarkdown,
        onMatureMarkdownToggle = {
            useMatureMarkdown = it
            scope.launch {
                settingsRepository.saveUseMatureMarkdown(it)
            }
        },
        onNavigateToAdvancedLlmSettings = onNavigateToAdvancedLlmSettings
    )
}

/**
 * Stateless content for LLM settings group.
 */
@Composable
fun LlmSettingsContent(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    localLlmAddress: String,
    onLocalLlmAddressChange: (String) -> Unit,
    useMatureMarkdown: Boolean,
    onMatureMarkdownToggle: (Boolean) -> Unit,
    onNavigateToAdvancedLlmSettings: () -> Unit,
) {
    SettingsGroup(title = stringResource(id = "settings_llm_title")) {
        ModelSelectionSetting()
        ApiKeySetting(
            apiKey = apiKey,
            onApiKeyChange = onApiKeyChange
        )

        LocalLlmAddressSetting(
            address = localLlmAddress,
            onAddressChange = onLocalLlmAddressChange
        )

        // Temporarily disabled while transitioning to Hybrid Markdown System
        /*
        MatureMarkdownToggleSetting(
            checked = useMatureMarkdown,
            onCheckedChange = onMatureMarkdownToggle
        )
        */

        AboutSetting(
            icon = Icons.Default.Tune,
            title = stringResource(id = "settings_advanced_title"),
            value = "",
            onClick = onNavigateToAdvancedLlmSettings
        )
    }
}

/**
 * Composable for selecting the LLM model.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModelSelectionSetting() {
    val settingsRepository = koinInject<SettingsRepository>()
    val scope = rememberCoroutineScope()

    val localModels = listOf(
        "Maiden-1.0-alpha",
        "Maiden-1.5-pro",
        "local-model"
    )

// TODO: use real gemini version
    val onlineModels = listOf("gemini")

    val models = localModels + onlineModels

    var expanded by remember { mutableStateOf(false) }
    var selectedModel by remember { mutableStateOf(models[0]) }

    LaunchedEffect(Unit) {
        settingsRepository.selectedModel.first()?.let { savedModel ->
            if (models.contains(savedModel)) {
                selectedModel = savedModel
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.AutoAwesome,
            contentDescription = stringResource(id = "settings_llm_model_version"),
            modifier = Modifier.padding(end = 16.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedModel,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = "settings_llm_default_model")) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                models.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(model) },
                        onClick = {
                            selectedModel = model
                            expanded = false
                            scope.launch {
                                settingsRepository.saveSelectedModel(model)
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Composable for entering the API key.
 */
@Composable
private fun ApiKeySetting(apiKey: String, onApiKeyChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = apiKey,
        onValueChange = onApiKeyChange,
        label = { Text(stringResource(id = "settings_llm_api_key")) },
        placeholder = { Text(stringResource(id = "settings_llm_api_key_placeholder")) },
        leadingIcon = {
            Icon(
                Icons.Default.Key,
                contentDescription = stringResource(id = "settings_llm_api_key")
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Composable for toggling the mature markdown engine.
 */
@Composable
private fun MatureMarkdownToggleSetting(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Verified,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Use Mature Markdown Engine",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Enable high-fidelity rendering for stable content.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

/**
 * Composable for configuring the local LLM address.
 */
@Composable
private fun LocalLlmAddressSetting(
    address: String,
    onAddressChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = address,
        onValueChange = onAddressChange,
        label = { Text(stringResource(id = "settings_advanced_llm_local_address_label")) },
        placeholder = { Text("http://192.168.1.x:1234/v1") },
        leadingIcon = { Icon(Icons.Default.Computer, contentDescription = null) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
