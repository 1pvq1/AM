package com.example.androidmaiden.screens.fileSystem.organize

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.Tag
import com.example.androidmaiden.screens.fileSystem.organize.components.*
import com.example.androidmaiden.viewModels.FileOrganizeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Entry point for the File Organize feature.
 * Connects the UI to the ViewModel.
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

    FileOrganizeScreen(
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
            if (file.isDirectory) {
                viewModel.navigateTo(file.path)
            }
        }
    )
}

/**
 * Stateless screen layout for the File Organize feature.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileOrganizeScreen(
    allTags: List<Tag>,
    selectedTag: Tag?,
    taggedFiles: List<FileMetadata>,
    searchQuery: String,
    searchResults: List<FileMetadata>,
    pathStack: List<String>,
    currentDirectoryFiles: List<FileMetadata>,
    onBack: () -> Unit,
    onSelectTag: (Tag?) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    onUpdateTag: (Tag) -> Unit,
    onUpdateSearchQuery: (String) -> Unit,
    onAddTagToFile: (FileMetadata, Tag) -> Unit,
    onRemoveTagFromFile: (FileMetadata, Tag) -> Unit,
    onCreateTag: (String, String) -> Unit,
    onIndexClick: (Int) -> Unit,
    onRootClick: () -> Unit,
    onFileClick: (FileMetadata) -> Unit
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
                com.example.androidmaiden.views.fileSys.PathBreadcrumbs(
                    pathStack = pathStack,
                    onIndexClick = onIndexClick,
                    onRootClick = onRootClick
                )

                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(currentDirectoryFiles) { file ->
                        FileTagCard(
                            file = file,
                            availableTags = allTags,
                            onAddTagToFile = onAddTagToFile,
                            onClick = { onFileClick(file) }
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

@Preview
@Composable
fun PreviewFileOrganize() {
    // Using FileOrganizeScreen directly in Preview to avoid Koin initialization issues with koinViewModel()
    FileOrganizeScreen(
        allTags = listOf(
            Tag(1, "Work", "#FF0000"),
            Tag(2, "Personal", "#00FF00"),
            Tag(3, "Important", "#0000FF")
        ),
        selectedTag = null,
        taggedFiles = emptyList(),
        searchQuery = "test",
        searchResults = listOf(
            FileMetadata(
                path = "/storage/emulated/0/Documents/report.pdf",
                name = "report.pdf",
                isDirectory = false,
                lastModified = 0,
                size = 1024 * 1024,
                parentPath = "/storage/emulated/0/Documents"
            )
        ),
        pathStack = listOf("Documents"),
        currentDirectoryFiles = listOf(
            FileMetadata(
                path = "/storage/emulated/0/Documents/report.pdf",
                name = "report.pdf",
                isDirectory = false,
                lastModified = 0,
                size = 1024 * 1024,
                parentPath = "/storage/emulated/0/Documents"
            )
        ),
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
