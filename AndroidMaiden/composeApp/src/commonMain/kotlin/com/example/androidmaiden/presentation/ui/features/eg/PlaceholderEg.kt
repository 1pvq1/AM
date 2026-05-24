package com.example.androidmaiden.views.eg

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Placeholder Screen Template
 * Used to provide a UI skeleton when features are not yet implemented,
 * facilitating testing of interactions and navigation.
 */
@Composable
fun PlaceholderScreen(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actions: List<String> = emptyList() // Optional: Display placeholder actions
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Page Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        // Brief Description
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        // Placeholder Action Buttons
        actions.forEach { action ->
            OutlinedButton(onClick = { /* TODO: Replace with real interaction */ }) {
                Text(action)
            }
        }

        // Placeholder Content Card
        Spacer(Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Placeholder Content", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("Specific functionality for \"$title\" will be displayed here.")
            }
        }
    }
}

/**
 * A simpler placeholder card for use within lists or grids.
 */
@Composable
fun PlaceholderCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "(Placeholder content, not yet implemented)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview
@Composable
fun PlaceholderScreenPreview() {
    PlaceholderScreen(
        title = "Sample Title",
        description = "This is a sample description for the placeholder screen.",
        actions = listOf("Action 1", "Action 2")
    )
}
