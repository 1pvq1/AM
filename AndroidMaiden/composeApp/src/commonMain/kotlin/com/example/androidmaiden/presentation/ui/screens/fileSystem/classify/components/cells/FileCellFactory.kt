package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components.cells

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.FileItem
import com.example.androidmaiden.presentation.ui.components.StickyDateHeader
import com.example.androidmaiden.presentation.ui.features.fileSys.ViewMode
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * Factory composable for creating file cells based on view mode and category.
 * Routes file item metadata to specialized atomic cell composables.
 *
 * @param viewMode Current layout view mode (LIST or GRID).
 * @param categoryType File classification category name ("Images", "Videos", "Audio", "Documents", "APKs", "Archives", "Other").
 * @param files List of [FileItem] metadata.
 * @param gridColumns Number of grid columns for GRID view mode.
 * @param onFileClick Callback when a file item is tapped.
 * @param onFileLongClick Callback when a file item is long pressed.
 */
@OptIn(ExperimentalTime::class, ExperimentalFoundationApi::class)
@Composable
fun FileCellFactory(
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
                        "Audio" -> AudioCell(file, ViewMode.GRID, modifier)
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
