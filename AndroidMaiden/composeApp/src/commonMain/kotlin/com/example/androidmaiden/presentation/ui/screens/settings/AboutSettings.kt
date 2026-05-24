package com.example.androidmaiden.presentation.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.screens.*
import com.example.androidmaiden.presentation.ui.components.*import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A group of settings providing information about the application.
 */
@Preview
@Composable
fun AboutSettingsGroup() {
    SettingsGroup(title = stringResource(id = "settings_about_title")) {
        AboutSetting(
            icon = Icons.Default.Info,
            title = stringResource(id = "settings_about_app_version"),
            value = "1.0.0-alpha01"
        )
        AboutSetting(
            icon = Icons.Default.Security,
            title = stringResource(id = "settings_about_privacy_policy"),
            value = "",
            onClick = { /* TODO: Navigate to Privacy Policy screen */ }
        )
    }
}

/**
 * Composable for a single informational setting row.
 */
@Composable
private fun AboutSetting(icon: ImageVector, title: String, value: String, onClick: (() -> Unit)? = null) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.padding(end = 16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (value.isNotEmpty()) {
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
