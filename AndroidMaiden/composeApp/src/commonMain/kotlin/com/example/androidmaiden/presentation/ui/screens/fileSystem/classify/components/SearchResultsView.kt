package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * View showing a list of search results for files.
 *
 * @param results List of file items matching the search query.
 * @param onFileClick Callback when a file result is clicked.
 */
@OptIn(ExperimentalTime::class)
@Composable
fun SearchResultsView(
    results: List<FileItem>,
    onFileClick: (FileItem) -> Unit
) {
    if (results.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No results found", color = MaterialTheme.colorScheme.outline)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(results) { file ->
                ListItem(
                    headlineContent = { Text(file.name) },
                    supportingContent = { Text(file.path, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    leadingContent = {
                        Icon(
                            imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth().clickable { onFileClick(file) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

/**
 * Light theme preview for the search results view.
 */
@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun SearchResultsViewLightPreview() {
    val sampleFiles = listOf(
        FileItem(
            path = "/path/to/file1.txt",
            name = "file1.txt",
            isDirectory = false,
            lastModified = 0,
            size = 1024,
            parentPath = "/path/to",
            createdAt = Instant.fromEpochMilliseconds(0)
        ),
        FileItem(
            path = "/path/to/folder",
            name = "folder",
            isDirectory = true,
            lastModified = 0,
            size = 0,
            parentPath = "/path/to",
            createdAt = Instant.fromEpochMilliseconds(0)
        )
    )
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            SearchResultsView(results = sampleFiles, onFileClick = {})
        }
    }
}
