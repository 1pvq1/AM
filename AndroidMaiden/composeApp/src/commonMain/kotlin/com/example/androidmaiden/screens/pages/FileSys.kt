package com.example.androidmaiden.screens.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun FileSysScreenPreview() {
    FilesScreen { }
}

data class FileFeature(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val screen: Screen? = null // Only features with a screen are navigable
)

@Composable
fun FilesScreen(onNavigate: (Screen) -> Unit) {
    val features = listOf(
        FileFeature("分析", "扫描并分析系统下文件结构", Icons.Default.Analytics, Screen.FileAnalysis),
        FileFeature("归类", "根据类型或规则自动标签与分类", Icons.Default.Style),
        FileFeature("整理", "优化用户的目录结构，提升可读性", Icons.Default.CreateNewFolder),
        FileFeature("清洁", "清理无用或重复文件", Icons.Default.CleaningServices)
    )
    val scrollState = rememberScrollState()
    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("文件管理", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("对您的文件系统进行分析、归类、整理和清洁。")

        Spacer(Modifier.height(16.dp))

        features.forEach { feature ->
            FileFeatureCard(feature = feature, onClick = { feature.screen?.let(onNavigate) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileFeatureCard(feature: FileFeature, onClick: () -> Unit) {
    val isClickable = feature.screen != null
    val cardColors = if (isClickable) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    }

    Card(
        onClick = { if (isClickable) onClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, hoveredElevation = 4.dp, pressedElevation = 1.dp),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null, // Decorative
                modifier = Modifier.size(40.dp),
                tint = if (isClickable) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = feature.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = feature.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isClickable) 1f else 0.6f)
                )
            }

            if (isClickable) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Details",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
