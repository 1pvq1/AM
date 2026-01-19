package com.example.androidmaiden.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable

@Composable
fun ShowDialogButton(showDialog: Boolean, onToggle: () -> Unit) {
    StyledButton(
        onClick = onToggle,
        icon = if (showDialog) Icons.AutoMirrored.Filled.Chat else Icons.Filled.Person,
        text = if (showDialog) "人物+对话" else "仅人物",
        tooltip = "Toggle Dialog"
    )
}

@Composable
fun SwitchLayoutButton(enabled: Boolean, onSwitch: () -> Unit) {
    StyledButton(
        onClick = onSwitch,
        enabled = enabled,
        icon = Icons.Filled.SwapHoriz,
        text = "切换布局",
        tooltip = "Switch Layout"
    )
}

@Composable
fun DevButton() {
    StyledButton(
        onClick = { /* TODO */ },
        icon = Icons.Filled.Build,
        text = "开发中",
        tooltip = "Under Development"
    )
}
