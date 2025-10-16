package com.example.androidmaiden.screenPages

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Feature(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String   // 点击后跳转的目标路由
)

@Preview
@Composable
fun SkillsPage(navController: NavController) {
    // 模拟功能列表（后续可从配置/后端/本地存储加载）
    val features = listOf(
        Feature(1, "任务管理", "添加、完成、删除任务"),
        Feature(2, "人物对话", "展示立绘和对话框"),
        Feature(3, "主题切换", "支持明暗模式"),
        Feature(4, "统计分析", "任务完成度统计"),
//        Feature(5, "技能区", "展示应用支持的功能"),
        Feature(5, "文件管理", "分析，归类，整理，清洁")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "技能区",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        // 动态渲染功能卡片
        features.forEach { feature ->
            FeatureCard(feature) {
                navController.navigate(feature.route)
            }
        }
    }
}

@Composable
fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(feature.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(feature.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


