package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

data class FileCategory(
    val name: String,
    val icon: ImageVector,
    val count: Int,
    val totalSizeMb: Int
)

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyPage(
    categories: List<FileCategory>,
    onCategoryClick: (FileCategory) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<FileCategory?>(null) }
    var showItemsDialog by remember { mutableStateOf(false) }

    fun sampleItemsFor(category: FileCategory): List<String> {
        val ext = when (category.name) {
            "Images" -> ".jpg"
            "Videos" -> ".mp4"
            "Audio" -> ".mp3"
            "Documents" -> ".pdf"
            "APKs" -> ".apk"
            "Archives" -> ".zip"
            else -> ".dat"
        }
        val max = kotlin.math.min(category.count.coerceAtLeast(0), 50)
        return (1..max).map { idx -> "${category.name.lowercase()}_${idx}${ext}" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Classify Files") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                FileCategoryCard(
                    category,
                    onClick = {
                        selectedCategory = category
                        showItemsDialog = true
                        onCategoryClick(category)
                    }
                )
            }
        }
        if (showItemsDialog && selectedCategory != null) {
            val cat = selectedCategory!!
            val itemsForCat = sampleItemsFor(cat)

            AlertDialog(
                onDismissRequest = { showItemsDialog = false },
                confirmButton = {
                    TextButton(onClick = { showItemsDialog = false }) {
                        Text("Close")
                    }
                },
                title = { Text(cat.name) },
                text = {
                    if (itemsForCat.isEmpty()) {
                        Text("No files found.")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 360.dp)
                        ) {
                            items(itemsForCat) { fileName ->
                                ListItem(
                                    headlineContent = { Text(fileName) }
                                )
                                Divider()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FileCategoryCard(category: FileCategory, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(category.name, style = MaterialTheme.typography.titleMedium)
                Text("${category.count} files • ${category.totalSizeMb} MB", style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileClassifyScreenPreview() {
    val sampleCategories = getSampleCategories()
    FileClassifyPage(
        categories = sampleCategories,
        onBack = {},
        onCategoryClick = {}
    )
}

fun getSampleCategories(): List<FileCategory> = listOf(
    FileCategory("Images", Icons.Default.Image, 245, 512),
    FileCategory("Videos", Icons.Default.Videocam, 87, 2048),
    FileCategory("Audio", Icons.Default.MusicNote, 64, 980),
    FileCategory("Documents", Icons.Default.Description, 120, 314),
    FileCategory("APKs", Icons.Default.Android, 32, 822),
    FileCategory("Archives", Icons.Default.Archive, 18, 420),
    FileCategory("Others", Icons.Default.Folder, 53, 141),

    FileCategory("Large Files", Icons.Default.Folder, 18, 7290),
    FileCategory("Recent", Icons.Default.Schedule, 53, 141)
)