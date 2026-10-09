package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.androidmaiden.core.util.*
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.components.*
import com.example.androidmaiden.presentation.ui.features.eg.*
import com.example.androidmaiden.presentation.ui.features.fileSys.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.CategorySubcategoryTabs
import com.example.androidmaiden.presentation.ui.theme.core.*
import coil3.compose.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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

/**
 * Factory for creating file cells based on view mode and category.
 */
@OptIn(ExperimentalTime::class, ExperimentalFoundationApi::class)
@Composable
private fun FileCellFactory(
    viewMode: ViewMode,
    categoryType: String,
    files: List<FileItem>,
    gridColumns: Int,
    onFileClick: (FileItem) -> Unit,
    onFileLongClick: (FileItem) -> Unit = {}
) {
    val isMedia = categoryType == "Images" || categoryType == "Videos"
    val useGrid = viewMode == ViewMode.GRID

    if (useGrid) {
        if (isMedia) {
            val groupedFiles = remember(files) {
                files.groupBy { file ->
                    val instant = Instant.fromEpochMilliseconds(file.lastModified)
                    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${local.month.name} ${local.year}"
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groupedFiles.forEach { (date, items) ->
                    item(span = { GridItemSpan(this.maxLineSpan) }) {
                        StickyDateHeader(date, count = items.size)
                    }
                    items(items) { file ->
                        val modifier = Modifier.combinedClickable(
                            onClick = { onFileClick(file) },
                            onLongClick = { onFileLongClick(file) }
                        )
                        when (categoryType) {
                            "Images" -> ImagesCell(file, ViewMode.GRID, modifier)
                            "Videos" -> VideosCell(file, ViewMode.GRID, modifier)
                            else -> GenericItemCell(file, ViewMode.GRID, modifier)
                        }
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files) { file ->
                    val modifier = Modifier.combinedClickable(
                        onClick = { onFileClick(file) },
                        onLongClick = { onFileLongClick(file) }
                    )
                    when (categoryType) {
                        "APKs" -> APKCell(file, ViewMode.GRID, modifier)
                        else -> GenericItemCell(file, ViewMode.GRID, modifier)
                    }
                }
            }
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            items(files) { file ->
                val modifier = Modifier.combinedClickable(
                    onClick = { onFileClick(file) },
                    onLongClick = { onFileLongClick(file) }
                )
                when (categoryType) {
                    "Images" -> ImagesCell(file, viewMode, modifier)
                    "Videos" -> VideosCell(file, viewMode, modifier)
                    "Audio" -> AudioCell(file, viewMode, modifier)
                    "Documents" -> DocumentsCell(file, viewMode, modifier)
                    "APKs" -> APKCell(file, viewMode, modifier)
                    "Archives" -> ArchiveCell(file, viewMode, modifier)
                    else -> GenericItemCell(file, viewMode, modifier)
                }
                if (viewMode == ViewMode.LIST) HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

/**
 * Generic item cell for non-specialized file types.
 */
@Composable
private fun GenericItemCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    if (viewMode == ViewMode.LIST) {
        ListItem(
            headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            supportingContent = { Text("${formatSize(file.size)} • ${file.path}") },
            leadingContent = {
                Icon(
                    imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.AutoMirrored.Filled.InsertDriveFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = modifier
        )
    } else {
        Card(modifier = modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.AutoMirrored.Filled.InsertDriveFile, null, modifier = Modifier.size(48.dp))
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

/**
 * Item cell for image files.
 */
@Composable
private fun ImagesCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    val subcategory = remember(file) { FileTypeUtils.getImageSubcategory(file) }
    if (viewMode == ViewMode.GRID) {
        Card(shape = MaterialTheme.shapes.small, modifier = modifier) {
            Box {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                ) {
                    Text(
                        file.name,
                        color = Color.White,
                        fontSize = 10.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { 
                val resolution = if (file.width != null && file.height != null) " • ${file.width}x${file.height}" else ""
                Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}$resolution • ${subcategory.displayName}") 
            },
            leadingContent = {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                    contentScale = ContentScale.Crop
                )
            },
            modifier = modifier
        )
    }
}

/**
 * Item cell for video files.
 */
@Composable
private fun VideosCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    val subcategory = remember(file) { FileTypeUtils.getVideoSubcategory(file) }
    if (viewMode == ViewMode.GRID) {
        Card(modifier = modifier.fillMaxWidth()) {
            Box {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(16/9f).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    Icons.Default.PlayCircle, null,
                    modifier = Modifier.align(Alignment.Center).size(32.dp),
                    tint = Color.White.copy(alpha = 0.8f)
                )
                
                val durationText = remember(file.duration) {
                    file.duration?.let { formatDuration(it) } ?: "00:00"
                }
                
                val resolutionText = remember(file.width, file.height) {
                    if (file.width != null && file.height != null) {
                        when {
                            file.height >= 2160 -> "4K"
                            file.height >= 1080 -> "FHD"
                            file.height >= 720 -> "HD"
                            else -> "${file.width}x${file.height}"
                        }
                    } else "Video"
                }

                Text(
                    "$resolutionText • $durationText",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                        .background(Color.Black.copy(0.6f), MaterialTheme.shapes.extraSmall).padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(file.name, modifier = Modifier.padding(4.dp), maxLines = 1, style = MaterialTheme.typography.labelSmall)
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { 
                val durationText = file.duration?.let { " • ${formatDuration(it)}" } ?: ""
                val resolution = if (file.width != null && file.height != null) " • ${file.width}x${file.height}" else ""
                Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}$durationText$resolution • ${subcategory.displayName}") 
            },
            leadingContent = {
                Box {
                    AsyncImage(
                        model = file.path,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        Icons.Default.PlayCircle, null,
                        modifier = Modifier.size(16.dp).align(Alignment.Center),
                        tint = Color.White
                    )
                }
            },
            modifier = modifier
        )
    }
}

/**
 * Item cell for audio files with smart subcategory display.
 */
@Composable
private fun AudioCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    val fileTypeColors = LocalFileTypeColors.current
    val subcategory = remember(file) { FileTypeUtils.getAudioSubcategory(file) }
    val isRecording = subcategory == AudioSubcategory.RECORDINGS

    ListItem(
        headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = {
            val author = file.artist ?: subcategory.displayName
            val album = file.album ?: ""
            val albumText = if (album.isNotBlank()) " • $album" else ""
            val bitrateText = file.bitrate?.let { " • ${it / 1000}kbps" } ?: ""
            val durationText = file.duration?.let { " • ${formatDuration(it)}" } ?: ""
            Text("$author$albumText$bitrateText$durationText • ${formatSize(file.size)}")
        },
        leadingContent = {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.extraSmall),
                    contentScale = ContentScale.Crop,
                    error = null
                )

                Icon(
                    imageVector = when (subcategory) {
                        AudioSubcategory.RECORDINGS -> Icons.Default.Mic
                        AudioSubcategory.RINGTONES -> Icons.Default.Notifications
                        AudioSubcategory.PODCASTS_AUDIOBOOKS -> Icons.AutoMirrored.Filled.MenuBook
                        else -> Icons.Default.Audiotrack
                    },
                    contentDescription = null,
                    tint = if (isRecording) MaterialTheme.colorScheme.secondary else fileTypeColors.audio,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        trailingContent = { 
            val durationText = file.duration?.let { formatDuration(it) } ?: "00:00"
            Text(durationText, style = MaterialTheme.typography.labelSmall) 
        },
        modifier = modifier
    )
}

/**
 * Item cell for document files with smart subcategory chip.
 */
@Composable
private fun DocumentsCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    val fileTypeColors = LocalFileTypeColors.current
    val ext = file.name.substringAfterLast(".").uppercase()
    val subcategory = remember(file) { FileTypeUtils.getDocumentSubcategory(file) }

    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)} • ${subcategory.displayName}") },
        leadingContent = {
            Surface(
                color = when(ext) {
                    "PDF" -> fileTypeColors.pdf
                    "DOC", "DOCX" -> fileTypeColors.doc
                    "XLS", "XLSX" -> fileTypeColors.xls
                    "TXT", "MD" -> fileTypeColors.txt
                    else -> MaterialTheme.colorScheme.secondary
                },
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(ext, color = Color.White, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        },
        modifier = modifier
    )
}

/**
 * Item cell for APK files.
 */
@Composable
private fun APKCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    val fileTypeColors = LocalFileTypeColors.current
    val subcategory = remember(file) { FileTypeUtils.getApkSubcategory(file) }

    if (viewMode == ViewMode.GRID) {
        Card(modifier = modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.Default.Android, null, modifier = Modifier.size(48.dp), tint = fileTypeColors.apk)
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(subcategory.displayName, style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = MaterialTheme.colorScheme.outline)
            }
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { Text("${subcategory.displayName} • ${formatSize(file.size)}") },
            leadingContent = { Icon(Icons.Default.Android, null, tint = fileTypeColors.apk) },
            modifier = modifier
        )
    }
}

/**
 * Item cell for archive files (ZIP, RAR, etc.).
 */
@Composable
private fun ArchiveCell(file: FileItem, viewMode: ViewMode, modifier: Modifier = Modifier) {
    val fileTypeColors = LocalFileTypeColors.current
    val subcategory = remember(file) { FileTypeUtils.getArchiveSubcategory(file) }

    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)} • ${subcategory.displayName}") },
        leadingContent = { Icon(Icons.Default.Archive, null, tint = fileTypeColors.archive) },
        modifier = modifier
    )
}

/*
 * --- Previews ---
*/

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
