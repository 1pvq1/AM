package com.example.androidmaiden.presentation.ui.screens.fileSystem.clean

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.clean.components.*
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Stateless content for the File Cleanup feature.
 *
 * @param stats The cleanup statistics.
 * @param onBack Callback for navigating back.
 * @param onClearDuplicates Callback to trigger clearing duplicate files.
 * @param onClearEmptyFolders Callback to trigger clearing empty folders.
 * @param onRestoreFromTrash Callback to restore a file from the recycle bin.
 * @param onDeletePermanently Callback to delete a file permanently from the recycle bin.
 * @param snackbarHostState The snackbar host state for displaying messages.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileCleanContent(
    stats: CleanupStats,
    onBack: () -> Unit,
    onClearDuplicates: () -> Unit,
    onClearEmptyFolders: () -> Unit,
    onRestoreFromTrash: (String) -> Unit,
    onDeletePermanently: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("File Cleanup") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CleanupSummaryCard(stats)
            }

            item {
                Text("Redundant Files", style = MaterialTheme.typography.titleMedium)
            }

            item {
                CleanupActionCard(
                    title = "Duplicate Files",
                    subtitle = "${stats.duplicateFiles.size} files found",
                    icon = Icons.Default.CopyAll,
                    onClick = onClearDuplicates
                )
            }

            item {
                CleanupActionCard(
                    title = "Empty Folders",
                    subtitle = "${stats.emptyFolders.size} folders found",
                    icon = Icons.Default.FolderOff,
                    onClick = onClearEmptyFolders
                )
            }

            item {
                Text("Recycle Bin", style = MaterialTheme.typography.titleMedium)
            }

            if (stats.trashEntries.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Recycle bin is empty", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(stats.trashEntries) { entry ->
                    TrashItemRow(
                        entry = entry,
                        onRestore = { onRestoreFromTrash(entry.trashPath) },
                        onDelete = { onDeletePermanently(entry.trashPath) }
                    )
                }
            }
        }
    }
}

/**
 * Light theme preview for the file cleanup content.
 */
@Preview
@Composable
fun FileCleanContentLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileCleanContent(
                stats = CleanupStats(),
                onBack = {},
                onClearDuplicates = {},
                onClearEmptyFolders = {},
                onRestoreFromTrash = {},
                onDeletePermanently = {},
                snackbarHostState = remember { SnackbarHostState() }
            )
        }
    }
}
