package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.model.*
import com.example.androidmaiden.utils.*
import com.example.androidmaiden.views.*
import com.example.androidmaiden.views.fileSys.ViewMode
import coil3.compose.AsyncImage
import kotlinx.datetime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

enum class SortBy { NAME, SIZE, DATE }

/**
 * FilesListPage needs to evolve from a simple list into a multi-functional viewer, the best
 * strategy is to move toward a Type-Aware Adaptive Layout. Instead of a "one-size-fits-all" list,
 * the UI should swap its "content cell" and "arrangement logic" based on the category it is
 * currently displaying.
 * */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun FilesListPage(categoryName: String, files: List<FileSysNode>, onBack: () -> Unit) {
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

    val sortedFiles = remember(files, sortOrder) {
        when (sortOrder) {
            SortBy.NAME -> files.sortedBy { it.name }
            SortBy.SIZE -> files.sortedByDescending { it.size ?: 0L }
            SortBy.DATE -> files.sortedByDescending { it.lastModified }
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
                    IconButton(onClick = {
                        viewMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                    }) {
                        Icon(
                            imageVector = if (viewMode == ViewMode.LIST) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = "Switch View"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (files.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No files found")
            }
        } else {
            Box(modifier = Modifier.padding(padding)) {
                FileCellFactory(viewMode, categoryType, sortedFiles)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun FileCellFactory(viewMode: ViewMode, categoryType: String, files: List<FileSysNode>) {
    val isMedia = categoryType == "Images" || categoryType == "Videos"

    if (viewMode == ViewMode.GRID && isMedia) {
        val groupedFiles = remember(files) {
            files.groupBy { file ->
                file.lastModified?.let { ms ->
                    val instant = Instant.fromEpochMilliseconds(ms)
                    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${local.month.name} ${local.year}"
                } ?: "UNKNOWN DATE"
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedFiles.forEach { (date, items) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    StickyDateHeader(date, count = items.size)
                }
                items(items) { file ->
                    when (categoryType) {
                        "Images" -> ImagesCell(file, ViewMode.GRID)
                        "Videos" -> VideosCell(file, ViewMode.GRID)
                        else -> GenericItemCell(file, ViewMode.GRID)
                    }
                }
            }
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            items(files) { file ->
                when (categoryType) {
                    "Images" -> ImagesCell(file, viewMode)
                    "Videos" -> VideosCell(file, viewMode)
                    "Audio" -> AudioCell(file, viewMode)
                    "Documents" -> DocumentsCell(file, viewMode)
                    "APKs" -> APKCell(file, viewMode)
                    "Archives" -> ArchiveCell(file, viewMode)
                    else -> GenericItemCell(file, viewMode)
                }
                if (viewMode == ViewMode.LIST) HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

/** Standard Detailed Project Presentation */
@Composable
private fun GenericItemCell(file: FileSysNode, viewMode: ViewMode) {
    if (viewMode == ViewMode.LIST) {
        ListItem(
            headlineContent = { Text(file.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            supportingContent = { Text("${formatSize(file.size)} • ${file.path ?: ""}") },
            leadingContent = {
                Icon(
                    imageVector = if (file.isFolder) Icons.Default.Folder else Icons.Default.InsertDriveFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    } else {
        Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.Default.InsertDriveFile, null, modifier = Modifier.size(48.dp))
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

/** Provides file display and layout for image types */
@Composable
private fun ImagesCell(file: FileSysNode, viewMode: ViewMode) {
    if (viewMode == ViewMode.GRID) {
        Card(shape = RoundedCornerShape(8.dp)) {
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
            supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}") },
            leadingContent = {
                AsyncImage(
                    model = file.path,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        )
    }
}

@Composable
private fun VideosCell(file: FileSysNode, viewMode: ViewMode) {
    if (viewMode == ViewMode.GRID) {
        Card(modifier = Modifier.fillMaxWidth()) {
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
                Text(
                    "HD • 00:42", // Mock metadata
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp).background(Color.Black.copy(0.6f), RoundedCornerShape(2.dp)).padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(file.name, modifier = Modifier.padding(4.dp), maxLines = 1, style = MaterialTheme.typography.labelSmall)
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}") },
            leadingContent = {
                Box {
                    AsyncImage(
                        model = file.path,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        Icons.Default.PlayCircle, null,
                        modifier = Modifier.align(Alignment.Center).size(16.dp),
                        tint = Color.White
                    )
                }
            }
        )
    }
}

@Composable
private fun AudioCell(file: FileSysNode, viewMode: ViewMode) {
    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("Artist • Album • 320kbps • ${formatSize(file.size)}") },
        leadingContent = { Icon(Icons.Default.MusicNote, null, tint = Color(0xFFE91E63)) },
        trailingContent = { Text("03:45", style = MaterialTheme.typography.labelSmall) }
    )
}

@Composable
private fun DocumentsCell(file: FileSysNode, viewMode: ViewMode) {
    val ext = file.name.substringAfterLast(".").uppercase()
    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}") },
        leadingContent = {
            Surface(
                color = when(ext) {
                    "PDF" -> Color(0xFFF44336)
                    "DOC", "DOCX" -> Color(0xFF2196F3)
                    "XLS", "XLSX" -> Color(0xFF4CAF50)
                    "TXT", "MD" -> Color(0xFF9C27B0)
                    else -> MaterialTheme.colorScheme.secondary
                },
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(ext, color = Color.White, modifier = Modifier.padding(4.dp), fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }
    )
}

@Composable
private fun APKCell(file: FileSysNode, viewMode: ViewMode) {
    if (viewMode == ViewMode.GRID) {
        Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.Default.Android, null, modifier = Modifier.size(48.dp), tint = Color(0xFF3DDC84))
                Text(file.name, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("v1.0.2", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = MaterialTheme.colorScheme.outline)
            }
        }
    } else {
        ListItem(
            headlineContent = { Text(file.name) },
            supportingContent = { Text("com.example.app • ${formatSize(file.size)}") },
            leadingContent = { Icon(Icons.Default.Android, null, tint = Color(0xFF3DDC84)) }
        )
    }
}

@Composable
private fun ArchiveCell(file: FileSysNode, viewMode: ViewMode) {
    ListItem(
        headlineContent = { Text(file.name) },
        supportingContent = { Text("${formatSize(file.size)} • ${formatDateTime(file.lastModified)}") },
        leadingContent = { Icon(Icons.Default.Archive, null, tint = Color(0xFFFF9800)) }
    )
}

private fun formatSize(size: Long?): String {
    if (size == null) return "Unknown"
    return if (size > 1024 * 1024) "${size / (1024 * 1024)} MB" else "${size / 1024} KB"
}

// --- Preview Sample Data ---
private object PreviewSamples {
    val now = 1715856000000L
    val lastMonth = now - 30L * 24 * 60 * 60 * 1000
    val twoMonthsAgo = now - 60L * 24 * 60 * 60 * 1000

    val images = listOf(
        FileSysNode("Beach_Sunset.jpg", 3500000, now, NodeType.FILE, path = "mock/beach.jpg"),
        FileSysNode("Mountain_View.png", 5200000, now - 100000, NodeType.FILE, path = "mock/mountain.png"),
        FileSysNode("Family_Dinner.webp", 1200000, lastMonth, NodeType.FILE, path = "mock/family.webp"),
        FileSysNode("Old_Memory.jpg", 800000, twoMonthsAgo, NodeType.FILE, path = "mock/old.jpg")
    )

    val videos = listOf(
        FileSysNode("Summer_Vacation.mp4", 150000000, now, NodeType.FILE, path = "mock/summer.mp4"),
        FileSysNode("Birthday_Party.mkv", 300000000, lastMonth, NodeType.FILE, path = "mock/birthday.mkv"),
        FileSysNode("Tutorial.mov", 50000000, lastMonth - 500000, NodeType.FILE, path = "mock/tutorial.mov")
    )

    val audio = listOf(
        FileSysNode("Song_One.mp3", 5000000, now, NodeType.FILE),
        FileSysNode("Podcast_Ep1.wav", 45000000, lastMonth, NodeType.FILE),
        FileSysNode("Voice_Note.m4a", 1000000, twoMonthsAgo, NodeType.FILE)
    )

    val documents = listOf(
        FileSysNode("Project_Report.pdf", 2500000, now, NodeType.FILE),
        FileSysNode("Budget_Plan.xlsx", 1200000, lastMonth, NodeType.FILE),
        FileSysNode("Notes.txt", 50000, twoMonthsAgo, NodeType.FILE),
        FileSysNode("Resume.docx", 800000, now - 3600000, NodeType.FILE)
    )

    val apks = listOf(
        FileSysNode("Game_Launcher.apk", 60000000, now, NodeType.FILE),
        FileSysNode("Messenger_Lite.apk", 15000000, lastMonth, NodeType.FILE)
    )

    val archives = listOf(
        FileSysNode("Photos_2023.zip", 500000000, twoMonthsAgo, NodeType.FILE),
        FileSysNode("Old_Projects.rar", 1200000000, lastMonth, NodeType.FILE),
        FileSysNode("Backup.7z", 250000000, now, NodeType.FILE)
    )
}

@Preview
@Composable
fun FileListPageImagesPreview() {
    FilesListPage("Images", PreviewSamples.images, {})
}

@Preview
@Composable
fun FileListPageVideosPreview() {
    FilesListPage("Videos", PreviewSamples.videos, {})
}

@Preview
@Composable
fun FileListPageAudioPreview() {
    FilesListPage("Audio", PreviewSamples.audio, {})
}

@Preview
@Composable
fun FileListPageDocumentsPreview() {
    FilesListPage("Documents", PreviewSamples.documents, {})
}

@Preview
@Composable
fun FileListPageAPKsPreview() {
    FilesListPage("APKs", PreviewSamples.apks, {})
}

@Preview
@Composable
fun FileListPageArchivesPreview() {
    FilesListPage("Archives", PreviewSamples.archives, {})
}
