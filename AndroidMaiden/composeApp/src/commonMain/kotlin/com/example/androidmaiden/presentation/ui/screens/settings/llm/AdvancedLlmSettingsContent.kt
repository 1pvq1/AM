package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.screens.pages.AppBarType
import com.example.androidmaiden.presentation.ui.screens.pages.BasePage
import com.example.androidmaiden.presentation.ui.screens.settings.llm.components.LocalModelsSection
import com.example.androidmaiden.presentation.ui.screens.settings.llm.components.OnlineModelsSection
import com.example.androidmaiden.presentation.viewmodel.AdvancedLlmSettingsUiState

/**
 * Stateless content of the Advanced LLM Settings page.
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

            item {
                LocalModelsSection(
                    localLlmAddress = uiState.localLlmAddress,
                    localApiKey = uiState.localApiKey,
                    localLlmStatus = uiState.localLlmStatus,
                    localHelpText = uiState.localHelpText,
                    localHelpLink = uiState.localHelpLink,
                    isCheckingLocalLlm = uiState.isCheckingLocalLlm,
                    onLocalLlmAddressChange = onLocalLlmAddressChange,
                    onLocalApiKeyChange = onLocalApiKeyChange,
                    onConnect = onConnect,
                    onManageModels = { showModelsList = true }
                )
            }

            item {
                OnlineModelsSection(
                    geminiApiKey = uiState.geminiApiKey,
                    onGeminiKeyChange = onGeminiKeyChange,
                    openaiApiKey = uiState.openaiApiKey,
                    onOpenAiKeyChange = onOpenAiKeyChange,
                    customProviderUrl = uiState.customProviderUrl,
                    onCustomUrlChange = onCustomUrlChange,
                    customProviderApiKey = uiState.customProviderApiKey,
                    onCustomKeyChange = onCustomKeyChange
                )
            }
        }
    }
}
