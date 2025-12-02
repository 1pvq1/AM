package com.example.androidmaiden.screenPages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidmaiden.composeapp.generated.resources.Res
import androidmaiden.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.androidmaiden.Greeting
import com.example.androidmaiden.ui.DevButton
import com.example.androidmaiden.ui.ShowDialogButton
import com.example.androidmaiden.ui.SwitchLayoutButton
import com.example.androidmaiden.views.character.*
import com.example.androidmaiden.views.panel.PanelOfTask

@Preview
@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { CharacterSection() }
        item { TaskSection() }
        item { OtherSection() }
        item { TestingEg() }
    }
}

@Composable
fun CharacterSection() {
    var showDialog by remember { mutableStateOf(true) }
    var layout by remember { mutableStateOf(CharacterLayout.Horizontal) }
    val dialogText = "\"欢迎回来，主人！请问有什么安排？\""

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BarCharacterSection(
            showDialog = showDialog,
            onShowDialogChange = { showDialog = it },
            layout = layout,
            onLayoutChange = { layout = it }
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showDialog) {
                    CharacterWithDialog(dialogText = dialogText, layout = layout)
                } else {
                    CharacterIllustrationBox()
                }
            }
        }
    }
}

@Composable
fun TaskSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "今日任务",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            PanelOfTask()
        }
    }
}

@Composable
fun OtherSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "其他功能区",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "公告",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "这里可以放公告、统计信息、活动入口等。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun TestingEg() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Testing Area",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Spacer(Modifier.height(16.dp))
                        Text("Compose: $greeting")
                    }
                }
            }
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
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
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
    }
}

@Composable
fun CharacterPreview() {
    CharacterWithDialog("[横向布局]\n\"欢迎回来，主人\"", layout = CharacterLayout.Horizontal)
    CharacterWithDialog("纵向布局", layout = CharacterLayout.Vertical)
    CharacterWithDialog("浮动布局", layout = CharacterLayout.Floating) // bugs
}
