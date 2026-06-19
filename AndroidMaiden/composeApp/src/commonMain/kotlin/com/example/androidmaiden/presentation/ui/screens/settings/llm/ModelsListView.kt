package com.example.androidmaiden.presentation.ui.screens.settings.llm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.network.ModelConfig
import com.example.androidmaiden.platform.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A dialog/overlay for managing available models.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelsListDialog(
    models: List<ModelConfig>,
    onDismiss: () -> Unit,
    onToggleEnabled: (String) -> Unit,
    onMove: (Int, Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        },
        title = { Text("Manage Models") },
        text = {
            Box(modifier = Modifier.heightIn(max = 400.dp)) {
                if (models.isEmpty()) {
                    Text("No models discovered yet. Try connecting first.", 
                         style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyColumn {
                        itemsIndexed(models) { index, config ->
                            ModelConfigItem(
                                config = config,
                                onToggle = { onToggleEnabled(config.id) }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ModelConfigItem(
    config: ModelConfig,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.DragHandle,
            contentDescription = "Reorder",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = config.id, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (config.isEnabled) "Enabled" else "Disabled",
                style = MaterialTheme.typography.labelSmall,
                color = if (config.isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
        Checkbox(
            checked = config.isEnabled,
            onCheckedChange = { onToggle() }
        )
    }
}

@Preview
@Composable
fun PreviewModelsListDialog(){

}