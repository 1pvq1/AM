package com.example.androidmaiden.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidmaiden.composeapp.generated.resources.*

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


@Preview
@Composable
fun AppNavigationRailPreview() {
    AppNavigationRail(
        currentScreen = Screen.Home,
        onScreenSelected = {},
        windowSizeClass = WindowSizeClass(WindowSizeCategory.Medium, WindowSizeCategory.Medium),
        onToggleLayout = {}
    )
}

@Composable
fun AppNavigationRail(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    windowSizeClass: WindowSizeClass,
    onToggleLayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isExpanded = windowSizeClass.widthCategory == WindowSizeCategory.Expanded
    val railWidth = if (isExpanded) 200.dp else 80.dp

    NavigationRail(
        modifier = modifier.width(railWidth),
        header = {
            AppNavigationHeader(isExpanded = isExpanded, onToggleLayout = onToggleLayout)
        }
    ) {
        Spacer(Modifier.height(16.dp))
        
        AppNavigationRailItem(
            selected = currentScreen is Screen.Home,
            onClick = { onScreenSelected(Screen.Home) },
            icon = Icons.Filled.Home,
            label = stringResource(id = "nav_home"),
            isExpanded = isExpanded
        )
        AppNavigationRailItem(
            selected = currentScreen is Screen.Skills,
            onClick = { onScreenSelected(Screen.Skills) },
            icon = Icons.Default.Star,
            label = stringResource(id = "nav_skills"),
            isExpanded = isExpanded
        )
        AppNavigationRailItem(
            selected = currentScreen is Screen.Settings,
            onClick = { onScreenSelected(Screen.Settings) },
            icon = Icons.Filled.Settings,
            label = stringResource(id = "nav_settings"),
            isExpanded = isExpanded
        )
    }
}

@Composable
private fun AppNavigationHeader(isExpanded: Boolean, onToggleLayout: () -> Unit) {
    if (isExpanded) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleLayout() }
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.am_app_icon),
                contentDescription = "Toggle Layout",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = "AM",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "AndroidMaiden",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                )
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onToggleLayout() }
                .padding(vertical = 16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.am_app_icon),
                contentDescription = "Toggle Layout",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "AM",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}

@Composable
private fun AppNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    isExpanded: Boolean
) {
    if (isExpanded) {
        // Horizontal layout for Expanded
        Surface(
            selected = selected,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            shape = MaterialTheme.shapes.medium,
            color = if (selected) MaterialTheme.colorScheme.secondaryContainer else androidx.compose.ui.graphics.Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    } else {
        // Vertical layout for Medium (Default Rail behavior)
        NavigationRailItem(
            selected = selected,
            onClick = onClick,
            icon = { Icon(icon, contentDescription = label) },
            label = { Text(label) }
        )
    }
}
