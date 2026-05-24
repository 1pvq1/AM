package com.example.androidmaiden.presentation.ui.features.eg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.features.character.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Example class for demonstration purposes.
 */
class Example {
}

/**
 * Preview for the common character section UI.
 */
@Preview
@Composable
fun CommonUITaskPanelPreview() {
    CommonUIBarCharacterSection(
        onShowDialogChange = {},
        showDialog = false,
        onLayoutChange = {},
        layout = CharacterLayout.Horizontal
    )
}

/**
 * Common UI component for the character section with toggle buttons.
 */
@Composable
fun CommonUIBarCharacterSection(
    onShowDialogChange: (Boolean) -> Unit,
    showDialog: Boolean,
    onLayoutChange: (CharacterLayout) -> Unit,
    layout: CharacterLayout
) {
    Column {
        // Show toggle button
        Button(onClick = { onShowDialogChange(!showDialog) }) {
            Text(if (showDialog) "\uD83D\uDCACShow only characters" else "\uD83D\uDC64Show characters + dialogue")
        }

        if (showDialog) {
            // Layout toggle button
            Button(onClick = {
                onLayoutChange(
                    if (layout == CharacterLayout.Horizontal) CharacterLayout.Vertical
                    else CharacterLayout.Horizontal
                )
            }) {
                Text("\uD83D\uDD04Switch layout")
            }
        }
        // Reserved 'In Development' Button
        OutlinedButton(onClick = { /* TODO: Feature under development */ }) {
            Text("\uD83D\uDEE0In development…")
        }
    }

}

/**
 * Common task panel UI for display in previews.
 */
@Preview
@Composable
fun CommonUITaskPanel() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Today's task",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Spacer(Modifier.height(8.dp))

        // Example Task List
        repeat(5) { index ->
            Text("Task ${index + 1}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
        }
    }
}
