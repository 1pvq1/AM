package com.example.androidmaiden.views.eg

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 占位页面模板
 * 用于在功能未完成时提供 UI 骨架，便于测试交互和导航
 */

@Composable
fun PlaceholderScreen(
    title: String,
    description: String,
    actions: List<String> = emptyList() // 可选：展示一些占位操作
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 页面标题
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        // 简要说明
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        // 占位操作按钮
        actions.forEach { action ->
            OutlinedButton(onClick = { /* TODO: 替换为真实交互 */ }) {
                Text(action)
            }
        }

        // 占位内容卡片
        Spacer(Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("占位内容", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("这里将展示 $title 的具体功能")
            }
        }
    }
}


@Composable
fun PlaceholderCard(
    title: String,
    description: String
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "（占位内容，尚未实现）",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}