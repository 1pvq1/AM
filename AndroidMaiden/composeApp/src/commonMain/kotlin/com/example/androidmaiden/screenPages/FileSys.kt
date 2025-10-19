package com.example.androidmaiden.screenPages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Screen
import com.example.androidmaiden.views.eg.PlaceholderCard
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun FileSysScreenPreview() {
    FilesScreen { it }
}


data class FileFeature(
    val title: String,
    val description: String,
    val screen: Screen? = null // 有跳转的功能才填
)

@Composable
fun FilesScreen(onNavigate: (Screen) -> Unit) {
    val features = listOf(
        FileFeature("分析", "扫描并分析系统下文件结构", Screen.FileAnalysis),
        FileFeature("归类", "根据类型或规则自动标签与分类"),
        FileFeature("整理", "优化用户的目录结构，提升可读性"),
        FileFeature("清洁", "清理无用或重复文件")
    )

    Column(
        Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("文件管理页面", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("这里将展示文件分析、归类、整理、清洁功能")

        Spacer(Modifier.height(8.dp))

        // 功能占位卡片
        features.forEach { feature ->
            if (feature.screen != null) {
                // ✅ 分析：真实入口
                FileFeatureCard(
                    title = feature.title,
                    description = feature.description,
                    onClick = { onNavigate(feature.screen) },
                    showDetail = true
                )
            } else {
                // ⏸ 其他：占位页面
                PlaceholderCard(
                    title = feature.title,
                    description = feature.description
                )
            }
        }
    }
}

@Composable
fun FileFeatureCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    showDetail: Boolean
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick // 未来可点击
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))

            if (showDetail) {
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onClick) {
                    Text("详细")
                }
            }
        }
    }
}