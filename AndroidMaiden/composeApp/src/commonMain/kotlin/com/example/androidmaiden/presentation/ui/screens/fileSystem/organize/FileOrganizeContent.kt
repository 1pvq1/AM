@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.fileSystem.organize

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.organize.components.*
import com.example.androidmaiden.presentation.ui.features.fileSys.PathBreadcrumbs
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Stateless screen layout for the File Organize feature.
 *
 * @param allTags List of all available tags.
 * @param selectedTag The currently selected tag for filtering.
 * @param taggedFiles List of files associated with the selected tag.
 * @param searchQuery The current search query string.
 * @param searchResults List of files matching the search query.
 * @param pathStack The current navigation path stack.
 * @param currentDirectoryFiles List of files in the current directory with their tags.
 * @param onBack Callback for navigating back.
 * @param onSelectTag Callback when a tag is selected or deselected.
 * @param onDeleteTag Callback to delete a tag.
 * @param onUpdateTag Callback to update a tag's properties.
 * @param onUpdateSearchQuery Callback when the search query is updated.
 * @param onAddTagToFile Callback to add a tag to a file.
 * @param onRemoveTagFromFile Callback to remove a tag from a file.
 * @param onCreateTag Callback to create a new tag.
 * @param onIndexClick Callback to navigate to a specific index in the path stack.
 * @param onRootClick Callback to navigate to the root directory.
 * @param onFileClick Callback when a file or directory is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileOrganizeContent(
    allTags: List<Tag>,
    selectedTag: Tag?,
    taggedFiles: List<FileItem>,
    searchQuery: String,
    searchResults: List<FileItem>,
    pathStack: List<String>,
    currentDirectoryFiles: List<FileWithTags>,
    onBack: () -> Unit,
    onSelectTag: (Tag?) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    onUpdateTag: (Tag) -> Unit,
    onUpdateSearchQuery: (String) -> Unit,
    onAddTagToFile: (FileItem, Tag) -> Unit,
    onRemoveTagFromFile: (FileItem, Tag) -> Unit,
    onCreateTag: (String, String) -> Unit,
    onIndexClick: (Int) -> Unit,
    onRootClick: () -> Unit,
    onFileClick: (FileWithTags) -> Unit
) {
    var showCreateTagDialog by remember { mutableStateOf(false) }
    var editingTag by remember { mutableStateOf<Tag?>(null) }
    var isSearching by remember { mutableStateOf(false) }
    var isEditTagsMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FileOrganizeTopBar(
                isSearching = isSearching,
                searchQuery = searchQuery,
                onSearchQueryChange = onUpdateSearchQuery,
                onToggleSearch = {
                    isSearching = !isSearching
                    if (!isSearching) onUpdateSearchQuery("")
                },
                onBack = onBack,
                isEditMode = isEditTagsMode,
                onToggleEditMode = { isEditTagsMode = !isEditTagsMode },
                onCreateTag = { 
                    editingTag = null
                    showCreateTagDialog = true 
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Tag selector row (extracted component)
            TagSelectorRow(
                tags = allTags,
                selectedTag = selectedTag,
                isEditMode = isEditTagsMode,
                onTagSelect = onSelectTag,
                onTagDelete = onDeleteTag,
                onTagEdit = {
                    editingTag = it
                    showCreateTagDialog = true
                }
            )

            HorizontalDivider()

            if (selectedTag != null) {
                // Files for selected tag (extracted component)
                TaggedFilesView(
                    tagName = selectedTag.name,
                    files = taggedFiles,
                    onRemoveTag = { file -> onRemoveTagFromFile(file, selectedTag) }
                )
            } else if (isSearching) {
                // Search results view
                TagSearchView(
                    query = searchQuery,
                    onQueryChange = onUpdateSearchQuery,
                    searchResults = searchResults,
                    onAddTagToFile = onAddTagToFile,
                    availableTags = allTags
                )
            } else {
                // Main content: PathBreadcrumbs and current directory files
                PathBreadcrumbs(
                    pathStack = pathStack,
                    onIndexClick = onIndexClick,
                    onRootClick = onRootClick
                )

                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(currentDirectoryFiles) { fileWithTags ->
                        FileTagCard(
                            file = fileWithTags.file,
                            tags = fileWithTags.tags,
                            availableTags = allTags,
                            onAddTagToFile = onAddTagToFile,
                            onClick = { onFileClick(fileWithTags) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateTagDialog) {
        CreateTagDialog(
            tag = editingTag,
            onDismiss = { 
                showCreateTagDialog = false
                editingTag = null
            },
            onConfirm = { name, color ->
                if (editingTag != null) {
                    onUpdateTag(editingTag!!.copy(name = name, colorHex = color))
                } else {
                    onCreateTag(name, color)
                }
                showCreateTagDialog = false
                editingTag = null
            }
        )
    }
}

/**
 * Top bar for the file organizer with search and tag management actions.
 *
 * @param isSearching Whether the search mode is active.
 * @param searchQuery The current search query string.
 * @param onSearchQueryChange Callback when the search query is updated.
 * @param onToggleSearch Callback to toggle search mode.
 * @param onBack Callback for the back button.
 * @param isEditMode Whether the tag edit mode is active.
 * @param onToggleEditMode Callback to toggle tag edit mode.
 * @param onCreateTag Callback to open the tag creation dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileOrganizeTopBar(
    isSearching: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    onBack: () -> Unit,
    isEditMode: Boolean,
    onToggleEditMode: () -> Unit,
    onCreateTag: () -> Unit
) {
    if (isSearching) {
        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search files...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onToggleSearch) {
                    Icon(Icons.Default.Close, contentDescription = "Close Search")
                }
            }
        )
    } else {
        TopAppBar(
            title = { Text("File Organizer") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onToggleSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = onToggleEditMode) {
                    Icon(
                        if (isEditMode) Icons.Default.Check else Icons.Default.Edit, 
                        contentDescription = "Edit Tags",
                        tint = if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onCreateTag) {
                    Icon(Icons.Default.Add, contentDescription = "Create Tag")
                }
            }
        )
    }
}

/**
 * Preview for the File Organize content in Light Theme.
 */
@Preview
@Composable
fun FileOrganizeContentLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileOrganizeContent(
                allTags = listOf(
                    Tag(1, "Work", "#FF0000"),
                    Tag(2, "Personal", "#00FF00")
                ),
                selectedTag = null,
                taggedFiles = emptyList(),
                searchQuery = "",
                searchResults = emptyList(),
                pathStack = listOf("Documents"),
                currentDirectoryFiles = emptyList(),
                onBack = {},
                onSelectTag = {},
                onDeleteTag = {},
                onUpdateTag = {},
                onUpdateSearchQuery = {},
                onAddTagToFile = { _, _ -> },
                onRemoveTagFromFile = { _, _ -> },
                onCreateTag = { _, _ -> },
                onIndexClick = {},
                onRootClick = {},
                onFileClick = {}
            )
        }
    }
}
