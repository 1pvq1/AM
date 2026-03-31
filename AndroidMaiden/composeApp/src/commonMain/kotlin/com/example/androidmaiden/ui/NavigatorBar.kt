package com.example.androidmaiden.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.androidmaiden.model.Screen
import com.example.androidmaiden.Res.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AppNavigationBarPreview() {
    AppNavigationBar(currentScreen = Screen.Home, onScreenSelected = {})
}


@Composable
fun AppNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen is Screen.Home,
            onClick = { onScreenSelected(Screen.Home) },
            icon = { Icon(Icons.Filled.Home, contentDescription = stringResource(id = "nav_home")) },
            label = { Text(stringResource(id = "nav_home")) }
        )
        NavigationBarItem(
            selected = currentScreen is Screen.Skills,
            onClick = { onScreenSelected(Screen.Skills) },
            icon = { Icon(Icons.Default.Star, contentDescription = stringResource(id = "nav_skills")) },
            label = { Text(stringResource(id = "nav_skills")) }
        )
        NavigationBarItem(
            selected = currentScreen is Screen.Settings,
            onClick = { onScreenSelected(Screen.Settings) },
            icon = { Icon(Icons.Filled.Settings, contentDescription = stringResource(id = "nav_settings")) },
            label = { Text(stringResource(id = "nav_settings")) }
        )
    }
}
