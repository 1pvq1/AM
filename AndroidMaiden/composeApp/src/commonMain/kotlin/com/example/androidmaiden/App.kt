package com.example.androidmaiden

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview


// 定义导航目的地
sealed class Screen(val title: String) {
    object Home : Screen("首页")
    object Skills : Screen("技能")
    object Settings : Screen("设置")
    object Files : Screen("文件管理")
    object FileAnalysis : Screen("文件分析")
}

@Composable
@Preview
fun App() {

    // 只负责调用平台实现
    PlatformApp()

}

@Composable
expect fun PlatformApp()