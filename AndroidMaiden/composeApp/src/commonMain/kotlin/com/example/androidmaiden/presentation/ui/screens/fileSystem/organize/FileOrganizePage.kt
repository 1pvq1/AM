@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.example.androidmaiden.presentation.ui.screens.fileSystem.organize

import androidx.compose.runtime.*
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.viewmodel.FileOrganizeViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Stateful entry point for the File Organize feature.
 * Connects the UI to the FileOrganizeViewModel.
 *
 * @param onBack Callback for navigating back to the previous screen.
 */
@Composable
fun FileOrganizePage(onBack: () -> Unit) {
    val viewModel: FileOrganizeViewModel = koinViewModel()
    val allTags by viewModel.allTags.collectAsState()
    val selectedTag by viewModel.selectedTag.collectAsState()
    val taggedFiles by viewModel.taggedFiles.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val pathStack by viewModel.pathStack.collectAsState()
    val currentDirectoryFiles by viewModel.currentDirectoryFiles.collectAsState()

    val windowSizeClass = LocalWindowSizeClass.current

    FileOrganizeCoordinator(
        windowSizeClass = windowSizeClass,
        allTags = allTags,
        selectedTag = selectedTag,
        taggedFiles = taggedFiles,
        searchQuery = searchQuery,
        searchResults = searchResults,
        pathStack = pathStack,
        currentDirectoryFiles = currentDirectoryFiles,
        onBack = onBack,
        onSelectTag = { viewModel.selectTag(it) },
        onDeleteTag = { viewModel.deleteTag(it) },
        onUpdateTag = { viewModel.updateTag(it) },
        onUpdateSearchQuery = { viewModel.updateSearchQuery(it) },
        onAddTagToFile = { file, tag -> viewModel.addTagToFile(file.path, tag) },
        onRemoveTagFromFile = { file, tag -> viewModel.removeTagFromFile(file.path, tag) },
        onCreateTag = { name, color -> viewModel.createTag(name, color) },
        onIndexClick = { viewModel.navigateToIndex(it) },
        onRootClick = { viewModel.navigateToRoot() },
        onFileClick = { file ->
            if (file.file.isDirectory) {
                viewModel.navigateTo(file.file.path)
            }
        }
    )
}
