package com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.components.BaseCard
import com.example.androidmaiden.presentation.viewmodel.StorageStats
import com.example.androidmaiden.core.util.formatSize
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Data class representing a file-related feature in the dashboard.
 */
data class FileFeature(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val screen: Screen? = null
)

/**
 * The Stateless UI for the Files screen.
 *
 * @param columns Number of columns to display features.
 * @param features List of available file features.
 * @param storageStats Current storage usage statistics.
 * @param onNavigate Callback for navigation.
 */
@Composable
fun FilesContent(
    columns: Int,
    features: List<FileFeature>,
    storageStats: StorageStats,
    onNavigate: (Screen) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(stringResource(id = "file_management"), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(stringResource(id = "file_management_description"))

        Spacer(Modifier.height(24.dp))
        
        // Storage Summary Section
        StorageSummaryCard(storageStats)

        Spacer(Modifier.height(24.dp))

        if (columns == 1) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.weight(1f).verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                features.forEach { feature ->
                    FileFeatureCard(feature, onNavigate)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(features) { feature ->
                    FileFeatureCard(feature, onNavigate)
                }
            }
        }
    }
}

/**
 * Card showing a summary of storage usage.
 */
@Composable
private fun StorageSummaryCard(stats: StorageStats) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Used Storage",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = formatSize(stats.totalSize),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "${stats.fileCount} Files • ${stats.folderCount} Folders",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        }
    }
}

/**
 * Card representing a single file feature.
 */
@Composable
private fun FileFeatureCard(feature: FileFeature, onNavigate: (Screen) -> Unit) {
    val isClickable = feature.screen != null
    BaseCard(
        title = feature.title,
        description = feature.description,
        icon = feature.icon,
        onClick = { feature.screen?.let(onNavigate) },
        isClickable = isClickable,
        trailingIcon = if (isClickable) Icons.AutoMirrored.Filled.ArrowForward else null,
        iconTint = MaterialTheme.colorScheme.secondary
    )
}

/**
 * Preview for the Files content.
 */
@Preview
@Composable
fun FileSysContentPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FilesContent(
                columns = 1,
                features = listOf(
                    FileFeature("Analyze", "Analyze your storage", Icons.Default.Analytics),
                    FileFeature("Clean", "Clean up temporary files", Icons.Default.CleaningServices)
                ),
                storageStats = StorageStats(totalSize = 1024L * 1024 * 1024 * 5, fileCount = 1200, folderCount = 150),
                onNavigate = {}
            )
        }
    }
}
