package com.example.androidmaiden.presentation.ui.features.fileSys

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
import com.example.androidmaiden.domain.model.BreadcrumbMaxSegments
import com.example.androidmaiden.domain.model.BreadcrumbRootLabel
import com.example.androidmaiden.domain.model.BreadcrumbSeparator
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Clickable breadcrumbs component for interactive hierarchical file path navigation.
 * Supports configurable separator symbols, root label styles, and path segment truncation.
 *
 * @param pathStack The current navigation stack of absolute or relative paths.
 * @param onIndexClick Callback for jumping to a specific breadcrumb index in the stack.
 * @param onRootClick Callback for returning directly to the root directory.
 * @param separatorStyle The separator style symbol to render between path segments.
 * @param maxSegments Maximum visible path segment length limit configuration.
 * @param rootLabel The display text label for the root node segment.
 */
@Composable
fun PathBreadcrumbs(
    pathStack: List<String>,
    onIndexClick: (Int) -> Unit,
    onRootClick: () -> Unit,
    separatorStyle: BreadcrumbSeparator = BreadcrumbSeparator.CHEVRON,
    maxSegments: BreadcrumbMaxSegments = BreadcrumbMaxSegments.UNLIMITED,
    rootLabel: BreadcrumbRootLabel = BreadcrumbRootLabel.ROOT
) {
    val scrollState = rememberScrollState()

    // Auto-scroll to the rightmost breadcrumb whenever the path stack updates
    LaunchedEffect(pathStack.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    // Determine segments to display based on maxSegments setting
    val totalCount = pathStack.size
    val maxCount = maxSegments.maxVisibleCount
    val isTruncated = maxCount > 0 && totalCount > maxCount

    // Determine range of visible indices
    val visibleIndices = if (isTruncated) {
        (totalCount - maxCount) until totalCount
    } else {
        0 until totalCount
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Root Segment
        Text(
            text = rootLabel.text,
            modifier = Modifier.clickable { onRootClick() },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (pathStack.isEmpty()) FontWeight.Bold else FontWeight.Medium
        )

        // Collapsed indicator segment if truncated
        if (isTruncated) {
            BreadcrumbSeparatorView(separatorStyle)
            Text(
                text = "...",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Render visible path segments
        visibleIndices.forEach { index ->
            val path = pathStack[index]
            BreadcrumbSeparatorView(separatorStyle)

            val rawSegment = path.substringAfterLast("/")
            val segmentName = when {
                rawSegment.isNotBlank() -> rawSegment
                path == "/" -> rootLabel.text
                else -> path
            }
            val isLast = index == totalCount - 1

            Text(
                text = segmentName,
                modifier = Modifier.clickable { onIndexClick(index) },
                color = if (isLast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * Helper composable to render the chosen breadcrumb separator style.
 *
 * @param separator The chosen [BreadcrumbSeparator] style.
 */
@Composable
private fun BreadcrumbSeparatorView(separator: BreadcrumbSeparator) {
    when (separator) {
        BreadcrumbSeparator.CHEVRON -> {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Separator",
                modifier = Modifier.size(16.dp).padding(horizontal = 2.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
        else -> {
            Text(
                text = separator.symbol,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

/**
 * Preview for the path breadcrumbs component.
 */
@Preview(showBackground = true)
@Composable
fun PathBreadcrumbsPreview() {
    val pathStack = listOf("/storage", "/storage/emulated", "/storage/emulated/0", "/storage/emulated/0/Android")
    PathBreadcrumbs(
        pathStack = pathStack,
        onIndexClick = {},
        onRootClick = {},
        separatorStyle = BreadcrumbSeparator.SLASH,
        maxSegments = BreadcrumbMaxSegments.UNLIMITED,
        rootLabel = BreadcrumbRootLabel.ROOT
    )
}
