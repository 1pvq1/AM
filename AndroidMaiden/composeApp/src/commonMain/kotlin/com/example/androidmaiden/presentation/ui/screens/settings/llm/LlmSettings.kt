package com.example.androidmaiden.presentation.ui.screens.settings.llm

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
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.screens.*
import com.example.androidmaiden.presentation.ui.components.*import com.example.androidmaiden.data.repository.SettingsRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

/**
 * Preview for the LLM settings group.
 */
@Preview
@Composable
fun PreviewLlmSettingsGroup() {
    LlmSettingsGroup(onNavigateToAdvancedLlmSettings = {})
}

/**
 * A group of settings related to Language Learning Models (LLMs).
 */
@Composable
fun LlmSettingsGroup(onNavigateToAdvancedLlmSettings: () -> Unit) {
    val settingsRepository = koinInject<SettingsRepository>()
    var apiKey by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        apiKey = settingsRepository.apiKey.first() ?: ""
    }

    SettingsGroup(title = stringResource(id = "settings_llm_title")) {
        ModelSelectionSetting()
        ApiKeySetting(
            apiKey = apiKey,
            onApiKeyChange = {
                apiKey = it
                scope.launch {
                    settingsRepository.saveApiKey(it)
                }
            }
        )

        LocalLlmAddressSetting(settingsRepository)

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
    val localModels = listOf(
        "Maiden-1.0-alpha",
        "Maiden-1.5-pro"
    )

    // TODO: use real gemini version
    val onlineModels = "gemini"

    val models = localModels + onlineModels

    // for testing, replace with actual models. Maiden-1.0-alpha is currently training, with deployment considered in the future
    var expanded by remember { mutableStateOf(false) }
    var selectedModel by remember { mutableStateOf(models[0]) }

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
                modifier = Modifier.menuAnchor().fillMaxWidth()
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
                            // TODO: Add callback for model change
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
        leadingIcon = { Icon(Icons.Default.Key, contentDescription = stringResource(id = "settings_llm_api_key")) },
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
 * Composable for configuring the local LLM address.
 */
@Composable
private fun LocalLlmAddressSetting(settingsRepository: SettingsRepository) {
    var address by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        address = settingsRepository.localLlmAddress.first()
    }

    OutlinedTextField(
        value = address,
        onValueChange = {
            address = it
            scope.launch {
                settingsRepository.saveLocalLlmAddress(it)
            }
        },
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
