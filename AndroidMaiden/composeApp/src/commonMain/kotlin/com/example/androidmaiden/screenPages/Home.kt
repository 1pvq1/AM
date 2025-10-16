package com.example.androidmaiden.screenPages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidmaiden.composeapp.generated.resources.Res
import androidmaiden.composeapp.generated.resources.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Greeting
import com.example.androidmaiden.ui.DevButton
import com.example.androidmaiden.ui.ShowDialogButton
import com.example.androidmaiden.ui.SwitchLayoutButton
import com.example.androidmaiden.views.CommonUITaskPanel
import com.example.androidmaiden.views.character.*
import com.example.androidmaiden.views.panel.PanelOfTask


@Preview
@Composable
fun HomeScreen() {
    ScrollablePage {
        CharacterSection() // 人物与对话区域
        Spacer(Modifier.height(24.dp))
        TaskSection() // 任务区域
        Spacer(Modifier.height(24.dp))
        OtherSection()  // 其他扩展区域
        Spacer(Modifier.height(24.dp))
        TestingEg()
    }
}

@Composable
fun ScrollablePage(content: @Composable ColumnScope.() -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun OtherSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "其他功能区",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Spacer(Modifier.height(8.dp))
        Text("这里可以放公告、统计信息、活动入口等。")
    }
}


@Composable
fun TaskSection() {
    PanelOfTask()
//    CommonUITaskPanel()
}


@Composable
fun CharacterSection() {
    // 状态：是否显示对话框
    var showDialog by remember { mutableStateOf(true) }
    // 状态：布局模式（水平/垂直）
    var layout by remember { mutableStateOf(CharacterLayout.Horizontal) }

    val dialogText = "\"欢迎回来，主人！请问有什么安排？\""

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 功能按钮区
        BarCharacterSection(
            showDialog = showDialog,
            onShowDialogChange = { showDialog = it },
            layout = layout,
            onLayoutChange = { layout = it })
//        // 显示：角色立绘、对话等
//        CharacterPreview() // dev view
        if (showDialog) {
            CharacterWithDialog(dialogText = dialogText, layout = layout)
        } else {
            CharacterIllustrationBox()
        }

    }
}

@Composable
private fun BarCharacterSection(
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    layout: CharacterLayout,
    onLayoutChange: (CharacterLayout) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        ShowDialogButton(showDialog) { onShowDialogChange(!showDialog) }

        SwitchLayoutButton(
            enabled = showDialog,
            onSwitch = {
                onLayoutChange(
                    if (layout == CharacterLayout.Horizontal)
                        CharacterLayout.Vertical
                    else
                        CharacterLayout.Horizontal
                )
            }
        )

        DevButton()


//        CommonUIBarCharacterSection(onShowDialogChange, showDialog, onLayoutChange, layout) //testing and preview
    }

}


@Composable
fun CharacterPreview() {
    CharacterWithDialog("[横向布局]\n\"欢迎回来，主人\"", layout = CharacterLayout.Horizontal)
    CharacterWithDialog("纵向布局", layout = CharacterLayout.Vertical)
    CharacterWithDialog("浮动布局", layout = CharacterLayout.Floating) // bugs
}


@Composable
private fun TestingEg() {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding().fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}
