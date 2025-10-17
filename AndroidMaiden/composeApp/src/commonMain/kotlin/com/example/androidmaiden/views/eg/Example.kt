package com.example.androidmaiden.views.eg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.views.character.CharacterLayout

class Example {
}

@Composable
fun CommonUIBarCharacterSection(
    onShowDialogChange: (Boolean) -> Unit,
    showDialog: Boolean,
    onLayoutChange: (CharacterLayout) -> Unit,
    layout: CharacterLayout
) {
    // 显示切换按钮
    Button(onClick = { onShowDialogChange(!showDialog) }) {
        Text(if (showDialog) "\uD83D\uDCAC只显示人物" else "\uD83D\uDC64显示人物+对话")
    }

    if (showDialog) {
        // 布局切换按钮
        Button(onClick = {
            onLayoutChange(
                if (layout == CharacterLayout.Horizontal) CharacterLayout.Vertical
                else CharacterLayout.Horizontal
            )
        }) {
            Text("\uD83D\uDD04切换布局")
        }
    }
    // 预留开发中按钮
    OutlinedButton(onClick = { /* TODO: 开发中功能 */ }) {
        Text("\uD83D\uDEE0开发中…")
    }
}

@Composable
fun CommonUITaskPanel() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "今日任务",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Spacer(Modifier.height(8.dp))

        // 示例任务列表
        repeat(5) { index ->
            Text("任务 ${index + 1}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
        }
    }
}