package com.example.androidmaiden.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.ButtonDisplayStyle
import com.example.androidmaiden.screens.SettingsGroup
import com.example.androidmaiden.screens.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewAppearanceSettingsGroup() {
    AppearanceSettingsGroup(
        previewThemeMode = ThemeMode.SYSTEM,
        onThemePreview = {},
        buttonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
        onButtonDisplayStyleChange = {}
    )
}

@Composable
fun AppearanceSettingsGroup(
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
) {
    SettingsGroup(title = "Appearance") {
        ThemeSetting(previewThemeMode, onThemePreview)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        ButtonDisplayStyleSetting(buttonDisplayStyle, onButtonDisplayStyleChange)
    }
}

@Composable
private fun ThemeSetting(previewThemeMode: ThemeMode, onThemePreview: (ThemeMode) -> Unit) {
    val options = listOf(
        ThemeMode.LIGHT to "Light", ThemeMode.DARK to "Dark", ThemeMode.SYSTEM to "System"
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Palette,
                contentDescription = "Theme",
                modifier = Modifier.padding(end = 16.dp)
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ButtonDisplayStyleSetting(
    currentStyle: ButtonDisplayStyle,
    onStyleChange: (ButtonDisplayStyle) -> Unit
) {
    val options = listOf(
        ButtonDisplayStyle.ICON_AND_TEXT to "Both",
        ButtonDisplayStyle.ICON_ONLY to "Icon Only",
        ButtonDisplayStyle.TEXT_ONLY to "Text Only"
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Apps,
                contentDescription = "Button Style",
                modifier = Modifier.padding(end = 16.dp)
            )
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
