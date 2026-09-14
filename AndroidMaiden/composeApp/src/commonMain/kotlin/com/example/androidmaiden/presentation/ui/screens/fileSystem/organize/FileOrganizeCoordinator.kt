package com.example.androidmaiden.presentation.ui.screens.fileSystem.organize

import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import kotlin.time.ExperimentalTime

/**
 * Adaptive Coordinator for the File Organize feature.
 *
 * @param windowSizeClass The current window size class of the device.
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
@ExperimentalTime
@Composable
fun FileOrganizeCoordinator(
    windowSizeClass: WindowSizeClass,
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
    // Organize UI currently uses a standard layout.
    // Future adaptive layouts for Expanded screens could include a persistent side panel for tags.
    FileOrganizeContent(
        allTags = allTags,
        selectedTag = selectedTag,
        taggedFiles = taggedFiles,
        searchQuery = searchQuery,
        searchResults = searchResults,
        pathStack = pathStack,
        currentDirectoryFiles = currentDirectoryFiles,
        onBack = onBack,
        onSelectTag = onSelectTag,
        onDeleteTag = onDeleteTag,
        onUpdateTag = onUpdateTag,
        onUpdateSearchQuery = onUpdateSearchQuery,
        onAddTagToFile = onAddTagToFile,
        onRemoveTagFromFile = onRemoveTagFromFile,
        onCreateTag = onCreateTag,
        onIndexClick = onIndexClick,
        onRootClick = onRootClick,
        onFileClick = onFileClick
    )
}
