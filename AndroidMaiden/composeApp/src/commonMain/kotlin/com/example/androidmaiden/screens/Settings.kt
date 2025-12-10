package com.example.androidmaiden.screens

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
import com.example.androidmaiden.ButtonDisplayStyle
import com.example.androidmaiden.screens.settings.AboutSettingsGroup
import com.example.androidmaiden.screens.settings.AppearanceSettingsGroup
import com.example.androidmaiden.screens.settings.ChatPersonalizationSettingsGroup
import com.example.androidmaiden.screens.settings.LlmSettingsGroup
import com.example.androidmaiden.screens.settings.NotificationsSettingsGroup
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
                AppearanceSettingsGroup(
                    previewThemeMode = previewThemeMode,
                    onThemePreview = onThemePreview,
                    buttonDisplayStyle = buttonDisplayStyle,
                    onButtonDisplayStyleChange = onButtonDisplayStyleChange
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Chat Personalization Section
            item {
                ChatPersonalizationSettingsGroup()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // LLM Settings Section
            item {
                LlmSettingsGroup(onNavigateToAdvancedLlmSettings)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Notifications Section
            item {
                NotificationsSettingsGroup()
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // About Section
            item {
                AboutSettingsGroup()
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
