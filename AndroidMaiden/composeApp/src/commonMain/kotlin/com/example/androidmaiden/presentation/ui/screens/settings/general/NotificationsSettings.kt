package com.example.androidmaiden.presentation.ui.screens.settings.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.screens.*
import com.example.androidmaiden.presentation.ui.components.*import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A group of settings for controlling application notifications.
 */
@Preview
@Composable
fun NotificationsSettingsGroup() {
    SettingsGroup(title = stringResource(id = "settings_notifications_title")) {
        NotificationSetting(
            icon = Icons.Default.Notifications,
            title = stringResource(id = "settings_notifications_enable_notifications"),
            description = stringResource(id = "settings_notifications_description")
        )
    }
}

/**
 * Composable for a single notification setting with a switch.
 */
@Composable
private fun NotificationSetting(icon: ImageVector, title: String, description: String) {
    var isChecked by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isChecked = !isChecked }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.padding(end = 16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = null
        )
    }
}
