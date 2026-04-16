package com.example.androidmaiden.screens.settings.llm

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.screens.SettingsGroup
import com.example.androidmaiden.screens.AboutSetting
import com.example.androidmaiden.data.SettingsHolder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewLlmSettingsGroup() {
    LlmSettingsGroup(onNavigateToAdvancedLlmSettings = {})
}

@Composable
fun LlmSettingsGroup(onNavigateToAdvancedLlmSettings: () -> Unit) {
    var apiKey by remember { mutableStateOf(SettingsHolder.apiKey ?: "") }

    SettingsGroup(title = stringResource(id = "settings_llm_title")) {
        ModelSelectionSetting()
        ApiKeySetting(
            apiKey = apiKey,
            onApiKeyChange = {
                apiKey = it
                SettingsHolder.apiKey = it
            }
        )

        if (apiKey.isNotBlank()) {
            // ModelProvidersSetting()
        }

        LocalLlmAddressSetting()

        AboutSetting(
            icon = Icons.Default.Tune,
            title = stringResource(id = "settings_advanced_title"),
            value = "",
            onClick = onNavigateToAdvancedLlmSettings
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModelSelectionSetting() {
    val LocalModels = listOf(
        "Maiden-1.0-alpha",
        "Maiden-1.5-pro"
    )

//    TODO: real gemini version
    val OnlineModels = "gemini"

    val models = LocalModels + OnlineModels

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

@Composable
private fun LocalLlmAddressSetting() {
    var address by remember { mutableStateOf(SettingsHolder.localLlmAddress) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = address,
        onValueChange = {
            address = it
            SettingsHolder.localLlmAddress = it
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
