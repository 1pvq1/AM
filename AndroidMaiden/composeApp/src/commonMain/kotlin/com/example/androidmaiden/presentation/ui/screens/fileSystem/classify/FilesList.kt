package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.androidmaiden.core.util.*
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.features.eg.*
import com.example.androidmaiden.presentation.ui.features.fileSys.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.CategorySubcategoryTabs
import com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.cells.FileCellFactory
import org.jetbrains.compose.ui.tooling.preview.*
import kotlin.time.*

/**
 * Enum for supported sorting options in the file list.
 */
enum class SortBy { NAME, SIZE, DATE }

/**
 * Internal state management for actions in FilesListPage.
 */
private class FileActionState(
    initialFile: FileItem? = null,
    initialShowDelete: Boolean = false,
    initialShowRename: Boolean = false
) {
    var file by mutableStateOf(initialFile)
    var showDelete by mutableStateOf(initialShowDelete)
    var showRename by mutableStateOf(initialShowRename)
    
    fun clear() {
        file = null
        showDelete = false
        showRename = false
    }
}

/**
 * FilesListPage provides a detailed view of files in a specific category.
 * Supports switching between List and Grid modes, sorting, smart subcategory filtering, and basic file operations.
 *
 * @param categoryName Name of the file classification category.
 * @param files List of [FileItem] models in the category.
 * @param onBack Callback when navigating back.
 * @param subcategories List of calculated [SubcategoryInfo] filters.
 * @param selectedSubcategoryId Currently active subcategory ID.
 * @param onSelectSubcategory Callback when selecting a subcategory filter.
 * @param onDelete Callback when requesting file deletion.
 * @param onRename Callback when requesting file rename.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class, ExperimentalFoundationApi::class)
@Composable
fun FilesListPage(
    categoryName: String, 
    files: List<FileItem>, 
    onBack: () -> Unit,
    subcategories: List<SubcategoryInfo> = emptyList(),
    selectedSubcategoryId: String? = null,
    onSelectSubcategory: (String?) -> Unit = {},
    onDelete: (FileItem) -> Unit = {},
    onRename: (FileItem, String) -> Unit = { _, _ -> }
) {
    val categoryType = remember(categoryName) {
        when {
            categoryName.contains("Images", ignoreCase = true) -> "Images"
            categoryName.contains("Videos", ignoreCase = true) -> "Videos"
            categoryName.contains("Audio", ignoreCase = true) -> "Audio"
            categoryName.contains("Documents", ignoreCase = true) -> "Documents"
            categoryName.contains("APKs", ignoreCase = true) -> "APKs"
            categoryName.contains("Archives", ignoreCase = true) -> "Archives"
            else -> "Other"
        }
    }

    val initialMode = remember(categoryType) {
        when (categoryType) {
            "Images", "Videos", "APKs" -> ViewMode.GRID
            else -> ViewMode.LIST
        }
    }

    var viewMode by remember { mutableStateOf(initialMode) }
    var sortOrder by remember { mutableStateOf(SortBy.DATE) }
    var showSortMenu by remember { mutableStateOf(false) }
    
    var gridColumns by remember { mutableIntStateOf(3) }
    var showGridDensityMenu by remember { mutableStateOf(false) }

    var previewFile by remember { mutableStateOf<FileItem?>(null) }
    
    // Action State Object
    val actionState = remember { FileActionState() }

    // Dynamic fallback calculation of subcategories if not supplied
    val activeSubcategories = remember(files, subcategories, categoryType) {
        if (subcategories.isNotEmpty()) subcategories
        else FileTypeUtils.calculateSubcategoriesForCategory(categoryType, files)
    }

    // Local subcategory selection state if controlled internally or synced externally
    var internalSubcategoryId by remember(selectedSubcategoryId) { mutableStateOf(selectedSubcategoryId) }

    val displayedFiles = remember(files, categoryType, internalSubcategoryId, sortOrder) {
        val filtered = FileTypeUtils.filterFilesBySubcategory(categoryType, internalSubcategoryId, files)
        when (sortOrder) {
            SortBy.NAME -> filtered.sortedBy { it.name }
            SortBy.SIZE -> filtered.sortedByDescending { it.size }
            SortBy.DATE -> filtered.sortedByDescending { it.lastModified }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Sort by Name") },
                                onClick = { sortOrder = SortBy.NAME; showSortMenu = false },
                                leadingIcon = { Icon(Icons.Default.SortByAlpha, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Size") },
                                onClick = { sortOrder = SortBy.SIZE; showSortMenu = false },
                                leadingIcon = { Icon(Icons.Default.Straighten, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Date") },
                                onClick = { sortOrder = SortBy.DATE; showSortMenu = false },
                                leadingIcon = { Icon(Icons.Default.CalendarToday, null) }
                            )
                        }
                    }
                    
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .size(40.dp)
                                .clip(CircleShape)
                                .combinedClickable(
                                    onClick = {
                                        viewMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                                    },
                                    onLongClick = {
                                        if (viewMode == ViewMode.GRID) showGridDensityMenu = true
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.ViewList,
                                contentDescription = "Switch View",
                                tint = if (showGridDensityMenu) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showGridDensityMenu,
                            onDismissRequest = { showGridDensityMenu = false }
                        ) {
                            Text(
                                "Grid Density",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            listOf(2, 3, 4).forEach { cols ->
                                DropdownMenuItem(
                                    text = { Text("$cols Columns") },
                                    onClick = {
                                        gridColumns = cols
                                        showGridDensityMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = if (gridColumns == cols) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = if (gridColumns == cols) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (activeSubcategories.isNotEmpty()) {
                CategorySubcategoryTabs(
                    subcategories = activeSubcategories,
                    selectedSubcategoryId = internalSubcategoryId,
                    onSelectSubcategory = { subId ->
                        internalSubcategoryId = subId
                        onSelectSubcategory(subId)
                    }
                )
            }

            if (displayedFiles.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (files.isEmpty()) "No files found" else "No matching items in this subcategory",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    FileCellFactory(
                        viewMode = viewMode,
                        categoryType = categoryType,
                        files = displayedFiles,
                        gridColumns = gridColumns,
                        onFileClick = { previewFile = it },
                        onFileLongClick = { actionState.file = it }
                    )
                }
            }
        }
    }

    // --- Overlays ---

    // 1. Preview
    previewFile?.let { file ->
        FilePreviewOverlay(
            file = file,
            onDismiss = { previewFile = null }
        )
    }
    
    // 2. Actions Bottom Sheet
    actionState.file?.let { file ->
        if (!actionState.showDelete && !actionState.showRename) {
            ModalBottomSheet(
                onDismissRequest = { actionState.file = null }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                    ListItem(
                        headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        supportingContent = { Text(formatSize(file.size)) },
                        leadingContent = { 
                            Icon(
                                imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            ) 
                        }
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = { Text("Rename") },
                        leadingContent = { Icon(Icons.Default.Edit, null) },
                        modifier = Modifier.clickable { actionState.showRename = true }
                    )
                    ListItem(
                        headlineContent = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                        leadingContent = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.clickable { actionState.showDelete = true }
                    )
                }
            }
        }
    }
    
    // 3. Delete Dialog
    if (actionState.showDelete) {
        AlertDialog(
            onDismissRequest = { actionState.showDelete = false },
            title = { Text("Delete File?") },
            text = { Text("Are you sure you want to delete '${actionState.file?.name}'? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        actionState.file?.let { onDelete(it) }
                        actionState.clear()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { actionState.showDelete = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // 4. Rename Dialog
    if (actionState.showRename) {
        var newName by remember { mutableStateOf(actionState.file?.name ?: "") }
        AlertDialog(
            onDismissRequest = { actionState.showRename = false },
            title = { Text("Rename File") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank() && newName != actionState.file?.name) {
                            actionState.file?.let { onRename(it, newName) }
                        }
                        actionState.clear()
                    }
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { actionState.showRename = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// --- Previews ---

@Preview
@Composable
fun FilesListPageGeneralPreview() {
    FilesListPage("General", FileListPagePreviewSamples.images, {})
}

@Preview
@Composable
fun FileListPageImagesPreview() {
    FilesListPage("Images", FileListPagePreviewSamples.images, {})
}

@Preview
@Composable
fun FileListPageVideosPreview() {
    FilesListPage("Videos", FileListPagePreviewSamples.videos, {})
}

@Preview
@Composable
fun FileListPageAudioPreview() {
    FilesListPage("Audio", FileListPagePreviewSamples.audio, {})
}

@Preview
@Composable
fun FileListPageDocumentsPreview() {
    FilesListPage("Documents", FileListPagePreviewSamples.documents, {})
}

@Preview
@Composable
fun FileListPageAPKsPreview() {
    FilesListPage("APKs", FileListPagePreviewSamples.apks, {})
}

@Preview
@Composable
fun FileListPageArchivesPreview() {
    FilesListPage("Archives", FileListPagePreviewSamples.archives, {})
}
