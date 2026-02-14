package com.example.androidmaiden.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Screen
import com.example.androidmaiden.ui.BaseCard
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Feature(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val screen: Screen? = null
)

@Preview
@Composable
fun SkillsPage(onNavigate: (Screen) -> Unit = {}) {
    // Feature list with icons
    val features = listOf(
        Feature(1, "任务管理", "添加、完成、删除任务", Icons.Default.Checklist, Screen.Todo),
        Feature(2, "人物交流", "展示立绘和对话框", Icons.Default.Forum, Screen.CharacterInteraction),
        Feature(3, "主题搭配", "自定义界面风格", Icons.Default.Palette),
        Feature(4, "统计分析", "任务完成度统计", Icons.Default.BarChart),
        Feature(5, "文件管理 ", "分析，归类，整理，清洁", Icons.Default.Folder, Screen.Files)
    )
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "技能区",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Render feature cards
        features.forEach { feature ->
            FeatureCard(feature) {
                feature.screen?.let { onNavigate(it) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    val isClickable = feature.screen != null
    BaseCard(
        title = feature.title,
        description = feature.description,
        icon = feature.icon,
        onClick = onClick,
        isClickable = isClickable,
        trailingIcon = if (isClickable) Icons.AutoMirrored.Filled.ArrowForward else null
    )
}
