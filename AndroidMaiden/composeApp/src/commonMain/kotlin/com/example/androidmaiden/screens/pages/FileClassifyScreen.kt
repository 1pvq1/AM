package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

data class FileCategory(
    val name: String,
    val icon: ImageVector,
    val count: Int,
    val totalSizeMb: Int
)

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileClassifyScreen(
    categories: List<FileCategory>,
    onCategoryClick: (FileCategory) -> Unit = {},
    onBack: () -> Unit = {}
) {

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
                FileCategoryCard(category, onClick = { onCategoryClick(category) })
            }
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
    val sampleCategories = listOf(
        FileCategory("Images", Icons.Default.Image, 245, 512),
        FileCategory("Videos", Icons.Default.Videocam, 87, 2048),
        FileCategory("Documents", Icons.Default.Description, 120, 314),
        FileCategory("APKs", Icons.Default.Android, 32, 822),
        FileCategory("Large Files", Icons.Default.Folder, 18, 7290),
        FileCategory("Recent", Icons.Default.Schedule, 53, 141)
    )
    FileClassifyScreen(
        categories = sampleCategories,
        onBack = {},
        onCategoryClick = {}
    )
}