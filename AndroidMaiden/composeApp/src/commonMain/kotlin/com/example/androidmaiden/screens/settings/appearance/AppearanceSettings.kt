package com.example.androidmaiden.screens.settings.appearance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.screens.SettingsGroup
import com.example.androidmaiden.ui.theme.isDynamicColorSupported
import com.example.androidmaiden.ui.theme.core.AppThemeType
import com.example.androidmaiden.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.ui.theme.core.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewAppearanceSettingsGroup() {
    AppearanceSettingsGroup(
        previewThemeMode = ThemeMode.SYSTEM,
        onThemePreview = {},
        currentThemeType = AppThemeType.DEFAULT,
        onThemeTypeChange = {},
        useDynamicColor = true,
        onDynamicColorChange = {},
        buttonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
        onButtonDisplayStyleChange = {}
    )
}

@Composable
fun AppearanceSettingsGroup(
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit,
    currentThemeType: AppThemeType,
    onThemeTypeChange: (AppThemeType) -> Unit,
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
) {
    SettingsGroup(title = stringResource(id = "settings_appearance_title")) {
        ThemeModeSetting(previewThemeMode, onThemePreview)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        
        val themePaletteEnabled = !useDynamicColor || !isDynamicColorSupported
        ThemeTypeSetting(
            currentThemeType = currentThemeType,
            onThemeTypeChange = onThemeTypeChange,
            enabled = themePaletteEnabled
        )
        
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        DynamicColorSetting(useDynamicColor, onDynamicColorChange)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        ButtonDisplayStyleSetting(buttonDisplayStyle, onButtonDisplayStyleChange)
    }
}

@Composable
private fun DynamicColorSetting(
    useDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit
) {
    val supported = isDynamicColorSupported
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .alpha(if (supported) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.ColorLens,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(id = "settings_appearance_dynamic_color"),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                if (supported) stringResource(id = "settings_appearance_dynamic_color_desc")
                else "Not supported on this device/version",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = useDynamicColor && supported,
            onCheckedChange = onDynamicColorChange,
            enabled = supported
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeTypeSetting(
    currentThemeType: AppThemeType,
    onThemeTypeChange: (AppThemeType) -> Unit,
    enabled: Boolean
) {
    val options = listOf(
        AppThemeType.DEFAULT to "Default",
        AppThemeType.MAIDEN to "Maiden",
        AppThemeType.TOKYO_NIGHT to "Tokyo Night"
    )

    var expanded by remember { mutableStateOf(false) }
    val currentLabel = options.find { it.first == currentThemeType }?.second ?: "Select Theme"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .alpha(if (enabled) 1f else 0.5f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Brush,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                "Theme Palette", 
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            
            Box {
                OutlinedCard(
                    onClick = { if (enabled) expanded = true },
                    enabled = enabled,
                    modifier = Modifier.widthIn(min = 120.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = currentLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Icon(
                            if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded && enabled,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { (type, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                onThemeTypeChange(type)
                                expanded = false
                            },
                            trailingIcon = {
                                if (currentThemeType == type) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        )
                    }
                }
            }
        }
        if (!enabled && isDynamicColorSupported) {
            Text(
                "Disabled while Dynamic Color is active",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 40.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeModeSetting(previewThemeMode: ThemeMode, onThemePreview: (ThemeMode) -> Unit) {
    val options = listOf(
        ThemeMode.LIGHT to stringResource(id = "settings_appearance_theme_light"),
        ThemeMode.DARK to stringResource(id = "settings_appearance_theme_dark"),
        ThemeMode.SYSTEM to stringResource(id = "settings_appearance_theme_system")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Palette,
                contentDescription = stringResource(id = "settings_appearance_theme"),
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(stringResource(id = "settings_appearance_theme"), style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, (mode, label) ->
                SegmentedButton(
                    selected = previewThemeMode == mode,
                    onClick = { onThemePreview(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (mode) {
                                ThemeMode.LIGHT -> Icons.Filled.LightMode
                                ThemeMode.DARK -> Icons.Filled.DarkMode
                                ThemeMode.SYSTEM -> Icons.Filled.SettingsBrightness
                            },
                            contentDescription = null,
                            modifier = Modifier.size(18.dp).padding(end = 4.dp)
                        )
                        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
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
        ButtonDisplayStyle.ICON_AND_TEXT to stringResource(id = "settings_appearance_button_style_both"),
        ButtonDisplayStyle.ICON_ONLY to stringResource(id = "settings_appearance_button_style_icon_only"),
        ButtonDisplayStyle.TEXT_ONLY to stringResource(id = "settings_appearance_button_style_text_only")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Apps,
                contentDescription = stringResource(id = "settings_appearance_button_style"),
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(stringResource(id = "settings_appearance_button_style"), style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, (style, label) ->
                SegmentedButton(
                    selected = currentStyle == style,
                    onClick = { onStyleChange(style) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f) // Distribute evenly
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (style) {
                                ButtonDisplayStyle.ICON_AND_TEXT -> Icons.Filled.Apps
                                ButtonDisplayStyle.ICON_ONLY -> Icons.Filled.Star // pick suitable icon
                                ButtonDisplayStyle.TEXT_ONLY -> Icons.Filled.FontDownload
                            },
                            contentDescription = null,
                            modifier = Modifier.size(18.dp).padding(end = 4.dp)
                        )
                        Text(
                            text = label,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
