package com.example.androidmaiden.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidmaiden.composeapp.generated.resources.Res
import androidmaiden.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.androidmaiden.Greeting
import com.example.androidmaiden.presentation.ui.components.DevButton
import com.example.androidmaiden.presentation.ui.components.ShowDialogButton
import com.example.androidmaiden.presentation.ui.components.SwitchLayoutButton
import com.example.androidmaiden.presentation.ui.features.character.*
import com.example.androidmaiden.presentation.ui.features.panel.PanelOfTask
import com.example.androidmaiden.presentation.ui.theme.core.LocalAppExtraShapes
import com.example.androidmaiden.presentation.ui.adaptive.*
import org.jetbrains.compose.resources.stringResource
import androidmaiden.composeapp.generated.resources.*
import com.example.androidmaiden.platform.stringResource

@Preview
@Composable
fun HomeScreen() {
    val windowSize = LocalWindowSizeClass.current

    if (windowSize.widthCategory == WindowSizeCategory.Expanded) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CharacterSection()
            }
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TaskSection()
                OtherSection()
            }
        }
    } else {
        val maxWidth = if (windowSize.widthCategory == WindowSizeCategory.Medium) 600.dp else Dp.Unspecified
        val modifier = if (maxWidth != Dp.Unspecified) Modifier.widthIn(max = maxWidth) else Modifier.fillMaxWidth()
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            LazyColumn(
                modifier = modifier.fillMaxHeight(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { CharacterSection() }
                item { TaskSection() }
                item { OtherSection() }
            }
        }
    }
}

@Composable
fun CharacterSection() {
    val windowSize = LocalWindowSizeClass.current
    val isExpanded = windowSize.widthCategory == WindowSizeCategory.Expanded
    
    var showDialog by remember { mutableStateOf(true) }
    var layout by remember { mutableStateOf(CharacterLayout.Vertical) }
    val dialogText = stringResource(id = "home_greeting_default")

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
            shape = LocalAppExtraShapes.current.characterBox,
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showDialog) {
                    CharacterWithDialog(dialogText = dialogText, layout = layout)
                } else {
                    val illustrationSize = if (isExpanded) 300.dp else 240.dp
                    CharacterIllustrationBox(modifier = Modifier.size(width = illustrationSize * 0.8f, height = illustrationSize))
                }
            }
        }
    }
}

@Composable
fun TaskSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = "home_daily_tasks"),
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
            text = stringResource(id = "home_other_features"),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id= "home_announcement"),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(id = "home_announcement_placeholder"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
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

