package com.example.androidmaiden.screenPages

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


//class Settings {
//}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        previewThemeMode = ThemeMode.DARK,
        onThemePreview = {}
    )
}

//@Preview
@Composable
fun SettingsScreen(previewThemeMode: ThemeMode, onThemePreview: (ThemeMode) -> Unit) {
    val options = listOf(
        ThemeMode.LIGHT to "浅色", ThemeMode.DARK to "深色", ThemeMode.SYSTEM to "跟随系统"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("设置", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
        // 方案一：SegmentedButton
        ThemeSegmentedBtn(options, previewThemeMode, onThemePreview)

        Spacer(Modifier.height(32.dp))
        // 方案二：Slider
        ThemeSlider(options, previewThemeMode, onThemePreview)
    }
    }
}


@Composable
private fun ThemeSegmentedBtn(
    options: List<Pair<ThemeMode, String>>,
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit
) {
    Text("主题设置（分段按钮）", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, (mode, label) ->
            SegmentedButton(
                modifier = Modifier.fillMaxHeight(0.10f),
                selected = previewThemeMode == mode,
                onClick = { onThemePreview(mode) },
                shape = SegmentedButtonDefaults.itemShape(index, options.size)
            ) {
                Text(label)
            }
        }
    }
}

@Composable
private fun ThemeSlider(
    options: List<Pair<ThemeMode, String>>,
    previewThemeMode: ThemeMode,
    onThemePreview: (ThemeMode) -> Unit
) {
    val currentIndex = ThemeMode.entries.indexOf(previewThemeMode)

    Text("主题设置（滑块选择）", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Slider(
        value = currentIndex.toFloat(),
        onValueChange = { newValue ->
            val index = newValue.toInt().coerceIn(0, ThemeMode.entries.toTypedArray().lastIndex)
            onThemePreview(ThemeMode.entries[index])
        },
        steps = ThemeMode.entries.size - 2,
        valueRange = 0f..(ThemeMode.entries.size - 1).toFloat()
    )
    Text("当前: ${options[currentIndex].second}", style = MaterialTheme.typography.bodyLarge)
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SettingsView(
//    previewThemeMode: ThemeMode,
//    onThemePreview: (ThemeMode) -> Unit,
//    currentSize: String,
//    onBack: () -> Unit,
//) {
//    val scrollState = rememberScrollState()
//    var expanded by remember { mutableStateOf(false) }
//
//    // 计算绝对路径
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//
//        Text("程序设置", style = MaterialTheme.typography.titleLarge)
//        Spacer(Modifier.height(16.dp))
//
//        OutlinedCard(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                // 主题设置

//        Spacer(Modifier.height(16.dp))
//
//        // 根目录设置
//
//        // --- 窗口大小设置 ---
//        OutlinedCard(
//            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.outlinedCardColors()
//        ) {
//            Column(Modifier.padding(16.dp)) {
//                Text("窗口大小设置", style = MaterialTheme.typography.titleMedium)
//
//
//            }
//        }
//
//
//    }
//}
