package com.example.androidmaiden.views.fileSys

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Clickable breadcrumbs for hierarchical navigation.
 *
 * @param pathStack The current stack of paths.
 * @param onIndexClick Callback for when a breadcrumb segment is clicked.
 * @param onRootClick Callback for when the root segment is clicked.
 */
@Composable
fun PathBreadcrumbs(
    pathStack: List<String>,
    onIndexClick: (Int) -> Unit,
    onRootClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Auto-scroll to the end when the path changes
    LaunchedEffect(pathStack.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Root",
            modifier = Modifier.clickable { onRootClick() },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (pathStack.isEmpty()) FontWeight.Bold else FontWeight.Normal
        )

        pathStack.forEachIndexed { index, path ->
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp).padding(horizontal = 4.dp),
                tint = MaterialTheme.colorScheme.outline
            )

            val name = path.substringAfterLast("/", "...")
            val isLast = index == pathStack.size - 1

            Text(
                text = name,
                modifier = Modifier.clickable { onIndexClick(index) },
                color = if (isLast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PathBreadcrumbsPreview(){
    val pathStack = listOf("Folder1", "Folder2", "File.txt")
}