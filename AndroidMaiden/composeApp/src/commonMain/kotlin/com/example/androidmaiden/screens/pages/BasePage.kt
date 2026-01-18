package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A base composable for creating pages with a consistent layout.
 *
 * This composable provides a `Scaffold` with a `MediumTopAppBar` that can be configured
 * to collapse on scroll. It allows for customization of the title, navigation icon,
 * and actions in the app bar.
 *
 * @param title The title to be displayed in the `TopAppBar`.
 * @param navigationIcon The `ImageVector` for the navigation icon. If null, no icon is shown.
 * @param onNavigationIconClick The lambda to be executed when the navigation icon is clicked.
 * @param actions A composable lambda for adding actions to the `TopAppBar`.
 * @param scrollBehavior A `TopAppBarScrollBehavior` to control the collapsing behavior of the app bar.
 * @param content A composable lambda that defines the main content of the page.
 *                It receives `PaddingValues` to apply appropriate padding from the `Scaffold`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasePage(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationIconClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldModifier = if (scrollBehavior != null) {
        modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } else {
        modifier
    }
    Scaffold(
        modifier = scaffoldModifier,
        topBar = {
            MediumTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (navigationIcon != null && onNavigationIconClick != null) {
                        IconButton(onClick = onNavigationIconClick) {
                            Icon(
                                imageVector = navigationIcon,
                                contentDescription = "Navigation Icon"
                            )
                        }
                    }
                },
                actions = actions,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                )
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

/**
 * A preview for the `BasePage` composable.
 *
 * This preview demonstrates how to use the `BasePage` with a title,
 * navigation icon, and a simple content area.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BasePagePreview() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    BasePage(
        title = "Preview Page",
        navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
        onNavigationIconClick = { /* Handle navigation icon click */ },
        scrollBehavior = scrollBehavior
    ) {
        // Content for the preview page can be placed here.
    }
}
