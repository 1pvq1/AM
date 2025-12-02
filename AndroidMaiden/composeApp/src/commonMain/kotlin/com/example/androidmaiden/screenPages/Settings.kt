package com.example.androidmaiden.screenPages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* 
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.example.androidmaiden.ButtonDisplayStyle

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        previewThemeMode = ThemeMode.SYSTEM,
        onThemePreview = {},
        buttonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
        onButtonDisplayStyleChange = {}
    )
}

@Composable
fun SettingsScreen(
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
            )
        }

        // Appearance Section
        item {
            SettingsGroup(title = "Appearance") {
                ThemeSetting(previewThemeMode, onThemePreview)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ButtonDisplayStyleSetting(buttonDisplayStyle, onButtonDisplayStyleChange)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Notifications Section
        item {
            SettingsGroup(title = "Notifications") {
                NotificationSetting(
                    icon = Icons.Default.Notifications,
                    title = "Enable Notifications",
                    description = "Receive alerts for task deadlines"
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // About Section
        item {
            SettingsGroup(title = "About") {
                AboutSetting(icon = Icons.Default.Info, title = "App Version", value = "1.0.0-alpha01")
                AboutSetting(
                    icon = Icons.Default.Security,
                    title = "Privacy Policy",
                    value = "",
                    onClick = { /* TODO: Navigate to Privacy Policy screen */ }
                )
            }
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ThemeSetting(previewThemeMode: ThemeMode, onThemePreview: (ThemeMode) -> Unit) {
    val options = listOf(
        ThemeMode.LIGHT to "Light", ThemeMode.DARK to "Dark", ThemeMode.SYSTEM to "System"
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Palette, contentDescription = "Theme", modifier = Modifier.padding(end = 16.dp))
            Text("Theme", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, (mode, label) ->
                SegmentedButton(
                    selected = previewThemeMode == mode,
                    onClick = { onThemePreview(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(label)
                }
            }
        }
    }
}

@Composable
fun ButtonDisplayStyleSetting(currentStyle: ButtonDisplayStyle, onStyleChange: (ButtonDisplayStyle) -> Unit) {
    val options = listOf(
        ButtonDisplayStyle.ICON_AND_TEXT to "Icon and Text",
        ButtonDisplayStyle.ICON_ONLY to "Icon Only",
        ButtonDisplayStyle.TEXT_ONLY to "Text Only"
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Apps, contentDescription = "Button Style", modifier = Modifier.padding(end = 16.dp))
            Text("Button Display Style", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, (style, label) ->
                SegmentedButton(
                    selected = currentStyle == style,
                    onClick = { onStyleChange(style) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(label)
                }
            }
        }
    }
}

@Composable
fun NotificationSetting(icon: ImageVector, title: String, description: String) {
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
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = null
        )
    }
}

@Composable
fun AboutSetting(icon: ImageVector, title: String, value: String, onClick: (() -> Unit)? = null) {
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
            Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
