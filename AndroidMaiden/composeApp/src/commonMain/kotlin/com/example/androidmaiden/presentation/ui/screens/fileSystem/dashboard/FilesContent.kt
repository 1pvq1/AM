package com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.components.BaseCard
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
 */
@Composable
fun FilesContent(
    columns: Int,
    features: List<FileFeature>,
    onNavigate: (Screen) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(stringResource(id = "file_management"), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(stringResource(id = "file_management_description"))

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

@Preview
@Composable
fun FileSysContentPreview() {
    FilesContent(
        columns = 1,
        features = listOf(
            FileFeature("Analyze", "Analyze your storage", Icons.Default.Analytics),
            FileFeature("Clean", "Clean up temporary files", Icons.Default.CleaningServices),
            FileFeature("Dev", "more features...", Icons.Default.DeveloperMode)

        ),
        onNavigate = {}
    )
}
