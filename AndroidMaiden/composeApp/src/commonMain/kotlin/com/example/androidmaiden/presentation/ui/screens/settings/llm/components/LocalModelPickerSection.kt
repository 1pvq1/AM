package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.LocalAiModel
import com.example.androidmaiden.domain.model.ModelFileFormat
import com.example.androidmaiden.presentation.viewmodel.LocalModelUiState

/**
 * UI section displaying local on-device AI models discovered on the device, custom folders, or AI Edge Gallery.
 * Prioritizes existing downloaded models to conserve bandwidth and storage space.
 *
 * @param uiState Local model state holding discovered and active models and granted folders.
 * @param onScanClick Action triggered when refreshing/scanning for models.
 * @param onSelectModel Action triggered when user selects a local model.
 * @param onImportClick Action triggered to open document picker or SAF.
 * @param onAddFolderClick Action triggered to pick a SAF directory tree (`ACTION_OPEN_DOCUMENT_TREE`).
 * @param modifier Optional layout modifier.
 */
@Composable
fun LocalModelPickerSection(
    uiState: LocalModelUiState,
    onScanClick: () -> Unit,
    onSelectModel: (String) -> Unit,
    onImportClick: () -> Unit,
    onAddFolderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Local Models",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "On-Device AI Models",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    IconButton(onClick = onAddFolderClick) {
                        Icon(
                            imageVector = Icons.Default.CreateNewFolder,
                            contentDescription = "Add Model Folder",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onImportClick) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = "Import Model File",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onScanClick, enabled = !uiState.isScanning) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Scan Models",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = "Scans downloaded models (.task, .bin, .tflite, .gguf, .onnx) from public storage, AI Edge Gallery, and user-added folder trees.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.grantedFolders.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Granted Folders (${uiState.grantedFolders.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    uiState.grantedFolders.forEach { folderUri ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = folderUri.substringAfterLast('%').take(40),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.isScanning) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Scanning model storage...", style = MaterialTheme.typography.bodySmall)
                }
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (uiState.availableModels.isEmpty() && !uiState.isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No on-device models found. Tap 'Add Folder' or 'Import File' to load models.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.availableModels.forEach { model ->
                        val isSelected = uiState.activeModel?.id == model.id
                        LocalModelItemRow(
                            model = model,
                            isSelected = isSelected,
                            onClick = { onSelectModel(model.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable row representing an individual local AI model item with source badge, format badge, and selection state.
 *
 * @param model Model metadata.
 * @param isSelected True if currently active.
 * @param onClick Selection handler.
 */
@Composable
private fun LocalModelItemRow(
    model: LocalAiModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = model.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Source tag badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = model.sourceTag,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    // Format badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (model.format) {
                                    ModelFileFormat.TASK -> MaterialTheme.colorScheme.tertiaryContainer
                                    ModelFileFormat.TFLITE -> MaterialTheme.colorScheme.primaryContainer
                                    ModelFileFormat.GGUF -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = model.format.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${model.getFormattedSize()} • ${model.path}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}
