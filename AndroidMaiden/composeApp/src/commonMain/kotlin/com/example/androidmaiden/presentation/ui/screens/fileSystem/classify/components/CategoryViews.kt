package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.model.FileCategory
import com.example.androidmaiden.core.util.FileTypeUtils
import com.example.androidmaiden.presentation.viewmodel.initialCategories
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * List layout for file categories.
 *
 * @param categories List of file categories to display.
 * @param onSelect Callback when a category is selected.
 */
@Composable
fun CategoryListView(
    categories: List<FileCategory>,
    onSelect: (FileCategory) -> Unit
) {
    val commonTypes = remember(categories) {
        val commonNames = FileTypeUtils.categoryDefinitions.map { it.name }
        categories.filter { it.name in commonNames }
    }
    val analysisTypes = remember(categories) {
        val analysisNames = FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }.map { it.name }
        categories.filter { it.name in analysisNames }
    }
    val otherTypes = remember(categories) {
        val knownNames = (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }).map { it.name }
        categories.filter { it.name !in knownNames }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (commonTypes.isNotEmpty()) {
            item { SectionHeader("Common Types") }
            items(commonTypes) { FileCategoryCard(it, onClick = { onSelect(it) }) }
        }

        if (analysisTypes.isNotEmpty()) {
            item { SectionHeader("Size and Date") }
            items(analysisTypes) { FileCategoryCard(it, onClick = { onSelect(it) }) }
        }

        if (otherTypes.isNotEmpty()) {
            item { SectionHeader("Other") }
            items(otherTypes) {
                FileCategoryCard(it, onClick = { onSelect(it) })
            }
        }
    }
}

/**
 * Grid layout for file categories.
 *
 * @param categories List of file categories to display.
 * @param onSelect Callback when a category is selected.
 */
@Composable
fun CategoryGridView(
    categories: List<FileCategory>,
    onSelect: (FileCategory) -> Unit
) {
    val commonTypes = remember(categories) {
        val commonNames = FileTypeUtils.categoryDefinitions.map { it.name }
        categories.filter { it.name in commonNames }
    }
    val analysisTypes = remember(categories) {
        val analysisNames = FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }.map { it.name }
        categories.filter { it.name in analysisNames }
    }
    val otherTypes = remember(categories) {
        val knownNames = (FileTypeUtils.categoryDefinitions + FileTypeUtils.analysisDefinitions.filter { it.type != "Other" }).map { it.name }
        categories.filter { it.name !in knownNames }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (commonTypes.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) { SectionHeader("Common Types") }
            items(commonTypes) { FileCategoryStrip(it, onClick = { onSelect(it) }) }
        }

        if (analysisTypes.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) { SectionHeader("Size and Date") }
            items(analysisTypes) { FileCategoryStrip(it, onClick = { onSelect(it) }) }
        }

        if (otherTypes.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) { SectionHeader("Other") }
            items(otherTypes) {
                FileCategoryStrip(it, onClick = { onSelect(it) })
            }
        }
    }
}

/**
 * A compact strip showing a single file category.
 *
 * @param category The file category data.
 * @param onClick Callback when the strip is clicked.
 */
@Composable
fun FileCategoryStrip(category: FileCategory, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${category.count ?: 0} items",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * A card showing a single file category with more details.
 *
 * @param category The file category data.
 * @param onClick Callback when the card is clicked.
 */
@Composable
fun FileCategoryCard(category: FileCategory, onClick: () -> Unit) {
    val description = if (category.count != null && category.totalSizeMb != null) {
        "${category.count} files • ${category.totalSizeMb} MB"
    } else {
        "Calculating..."
    }
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(category.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * Header for a section in the category list or grid.
 *
 * @param title The title of the section.
 */
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * Light theme preview for the category list view.
 */
@Preview
@Composable
fun CategoryListViewLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            CategoryListView(initialCategories) {}
        }
    }
}

/**
 * Dark theme preview for the category list view.
 */
@Preview
@Composable
fun CategoryListViewDarkPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            CategoryListView(initialCategories) {}
        }
    }
}

/**
 * Light theme preview for the category grid view.
 */
@Preview
@Composable
fun CategoryGridViewLightPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            CategoryGridView(initialCategories) {}
        }
    }
}

/**
 * Dark theme preview for the category grid view.
 */
@Preview
@Composable
fun CategoryGridViewDarkPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            CategoryGridView(initialCategories) {}
        }
    }
}
