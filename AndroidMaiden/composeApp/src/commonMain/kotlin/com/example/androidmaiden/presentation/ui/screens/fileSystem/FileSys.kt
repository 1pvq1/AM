package com.example.androidmaiden.presentation.ui.screens.fileSystem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.components.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the File System management screen.
 */
@Preview
@Composable
fun FileSysScreenPreview() {
    FilesScreen { }
}

/**
 * Data class representing a file-related feature in the dashboard.
 */
data class FileFeature(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val screen: Screen? = null // Only features with a screen are navigable
)

/**
 * The main entry point for file management features.
 */
@Composable
fun FilesScreen(onNavigate: (Screen) -> Unit) {
    val features = listOf(
        FileFeature(stringResource(id = "analyze"), stringResource(id = "analyze_description"), Icons.Default.Analytics, Screen.FileAnalysis),
        FileFeature(stringResource(id = "classify"), stringResource(id = "classify_description"), Icons.Default.Style, Screen.FileClassify),
        FileFeature(stringResource(id = "organize"), stringResource(id = "organize_description"), Icons.Default.CreateNewFolder, Screen.FileOrganize),
        FileFeature(stringResource(id = "clean"), stringResource(id = "clean_description"), Icons.Default.CleaningServices, Screen.FileClean)
    )
    val scrollState = rememberScrollState()

    // Request storage permission when the page is entered
    RequestStoragePermission()

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(id = "file_management"), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(stringResource(id = "file_management_description"))

        Spacer(Modifier.height(16.dp))

        features.forEach { feature ->
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
    }
}
