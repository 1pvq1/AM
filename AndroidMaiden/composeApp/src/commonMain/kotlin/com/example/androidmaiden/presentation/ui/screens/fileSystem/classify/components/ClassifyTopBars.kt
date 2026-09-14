package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Top bar for the classification page with sync and view toggle actions.
 *
 * @param isSyncing Whether a file scan is currently in progress.
 * @param viewMode The current view mode (List or Grid).
 * @param onBack Callback when the back button is clicked.
 * @param onSync Callback to trigger a manual file re-scan.
 * @param onToggleView Callback to switch between List and Grid view modes.
 * @param onSearchOpen Callback to open the search interface.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    isSyncing: Boolean,
    viewMode: ViewMode,
    onBack: () -> Unit,
    onSync: () -> Unit,
    onToggleView: () -> Unit,
    onSearchOpen: () -> Unit
) {
    TopAppBar(
        title = { Text("Classify Files") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onSearchOpen) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            
            val rotation by animateFloatAsState(
                targetValue = if (isSyncing) 360f else 0f,
                animationSpec = if (isSyncing) {
                    infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                } else {
                    tween(0)
                }
            )

            IconButton(onClick = onSync, enabled = !isSyncing) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Re-scan",
                    modifier = Modifier.rotate(rotation)
                )
            }

            IconButton(onClick = onToggleView) {
                Icon(
                    imageVector = if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.List,
                    contentDescription = "Switch View"
                )
            }
        }
    )
}

/**
 * Top bar displayed when search is active in the classification screen.
 *
 * @param query The current search query string.
 * @param onQueryChange Callback when the search query is updated.
 * @param onClose Callback to exit the search mode.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search files...") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close Search")
            }
        },
        actions = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear text")
                }
            }
        }
    )
}

/**
 * Light theme preview for the main top bar.
 */
@Preview
@Composable
fun MainTopBarLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        MainTopBar(
            isSyncing = false,
            viewMode = ViewMode.LIST,
            onBack = {},
            onSync = {},
            onToggleView = {},
            onSearchOpen = {}
        )
    }
}

/**
 * Dark theme preview for the main top bar.
 */
@Preview
@Composable
fun MainTopBarDarkPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        MainTopBar(
            isSyncing = true,
            viewMode = ViewMode.GRID,
            onBack = {},
            onSync = {},
            onToggleView = {},
            onSearchOpen = {}
        )
    }
}

/**
 * Light theme preview for the search top bar.
 */
@Preview
@Composable
fun SearchTopBarLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        SearchTopBar(
            query = "test",
            onQueryChange = {},
            onClose = {}
        )
    }
}
