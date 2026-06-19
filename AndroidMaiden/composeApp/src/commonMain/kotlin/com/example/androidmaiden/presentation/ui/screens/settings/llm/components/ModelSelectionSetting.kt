package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.platform.stringResource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Composable for selecting the LLM model.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectionSetting() {
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
