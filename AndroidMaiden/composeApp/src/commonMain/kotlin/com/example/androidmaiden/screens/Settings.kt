package com.example.androidmaiden.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        previewThemeMode = ThemeMode.SYSTEM,
        onThemePreview = {},
        buttonDisplayStyle = ButtonDisplayStyle.ICON_ONLY,
        onButtonDisplayStyleChange = {},
        onNavigateToAdvancedLlmSettings = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit,
    buttonDisplayStyle: ButtonDisplayStyle,
    onButtonDisplayStyleChange: (ButtonDisplayStyle) -> Unit,
    onNavigateToAdvancedLlmSettings: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { isSearchActive = false },
            active = isSearchActive,
            onActiveChange = { isSearchActive = it },
            placeholder = { Text("Search settings") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (isSearchActive) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchQuery.isNotEmpty()) searchQuery = "" else isSearchActive =
                                false
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close search"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        ) {
            // TODO: Implement search results 
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // Appearance Section
            item {
                SettingsGroup(title = "Appearance") {
                    ThemeSetting(previewThemeMode, onThemePreview)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ButtonDisplayStyleSetting(buttonDisplayStyle, onButtonDisplayStyleChange)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Chat Personalization Section
            item {
                var chatBgColor by remember { mutableStateOf(Color.Transparent) }

                SettingsGroup(title = "Chat Personalization") {
                    ChatPreview(backgroundColor = if (chatBgColor == Color.Transparent) MaterialTheme.colorScheme.surface else chatBgColor)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ChatBackgroundSetting(
                        selectedColor = chatBgColor,
                        onColorSelected = { chatBgColor = it }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    UserAvatarSetting()
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // LLM Settings Section
            item {
                SettingsGroup(title = "LLM Settings") {
                    AboutSetting(
                        icon = Icons.Default.AutoAwesome,
                        title = "Model Version",
                        value = "Maiden-1.0-alpha"
                    )
                    ApiKeySetting()
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    AboutSetting(
                        icon = Icons.Default.Tune,
                        title = "Advanced Settings",
                        value = "",
                        onClick = onNavigateToAdvancedLlmSettings
                    )
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
                    AboutSetting(
                        icon = Icons.Default.Info,
                        title = "App Version",
                        value = "1.0.0-alpha01"
                    )
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
}

@Composable
fun ApiKeySetting() {
    var apiKey by remember { mutableStateOf("") }

    OutlinedTextField(
        value = apiKey,
        onValueChange = { apiKey = it },
        label = { Text("API Key") },
        placeholder = { Text("Enter your API key") },
        leadingIcon = { Icon(Icons.Default.Key, contentDescription = "API Key") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
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

@Composable
fun ButtonDisplayStyleSetting(
    currentStyle: ButtonDisplayStyle,
    onStyleChange: (ButtonDisplayStyle) -> Unit
) {
    val options = listOf(
        ButtonDisplayStyle.ICON_AND_TEXT to "Icon and Text",
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

@Composable
fun ChatPreview(backgroundColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(100.dp),
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.align(Alignment.End).size(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {}
            Surface(
                shape = CircleShape,
                modifier = Modifier.align(Alignment.Start).size(24.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {}
            Text(
                "preview",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    .padding(4.dp).align(Alignment.End)
            )
        }
    }
}


@Composable
fun ChatBackgroundSetting(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.Transparent, // System Default
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Wallpaper,
                contentDescription = "Chat Background",
                modifier = Modifier.padding(end = 16.dp)
            )
            Text("Chat Background", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { color ->
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onColorSelected(color) },
                    shape = CircleShape,
                    color = if (color == Color.Transparent) MaterialTheme.colorScheme.surface else color,
                    border = if (selectedColor == color) BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    ) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    if (color == Color.Transparent) {
                        Icon(
                            Icons.Default.BrightnessAuto,
                            contentDescription = "System Default",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = { /* TODO: Handle image picking */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Image,
                contentDescription = "Choose Image",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Choose from gallery")
        }
    }
}

@Composable
fun UserAvatarSetting() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Your Avatar",
                modifier = Modifier.padding(end = 16.dp)
            )
            Text("Your Avatar", style = MaterialTheme.typography.bodyLarge)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Me",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* TODO: Handle avatar upload */ }) {
                Text("Upload")
            }
        }
    }
}

@Composable
fun SliderSetting(icon: ImageVector, title: String, description: String) {
    var sliderPosition by remember { mutableFloatStateOf(0.5f) }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, modifier = Modifier.padding(end = 16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
        Text(
            description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            modifier = Modifier.fillMaxWidth()
        )
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
