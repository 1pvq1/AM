package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.presentation.ui.screens.settings.llm.components.*
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
        onNavigateToAdvancedLlmSettings = {},
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
        onNavigateToAdvancedLlmSettings = {},
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
        val providerId = settingsRepository.selectedProviderId.first()
        apiKey = when (providerId) {
            "gemini" -> settingsRepository.geminiApiKey.first()
            "openai" -> settingsRepository.openaiApiKey.first()
            "custom" -> settingsRepository.customProviderApiKey.first()
            else -> ""
        }
        localLlmAddress = settingsRepository.localLlmAddress.first()
        useMatureMarkdown = settingsRepository.useMatureMarkdown.first()
    }

    LlmSettingsContent(
        apiKey = apiKey,
        onApiKeyChange = {
            apiKey = it
            scope.launch {
                val providerId = settingsRepository.selectedProviderId.first()
                when (providerId) {
                    "gemini" -> settingsRepository.saveGeminiApiKey(it)
                    "openai" -> settingsRepository.saveOpenAiApiKey(it)
                    "custom" -> settingsRepository.saveCustomProviderApiKey(it)
                }
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
