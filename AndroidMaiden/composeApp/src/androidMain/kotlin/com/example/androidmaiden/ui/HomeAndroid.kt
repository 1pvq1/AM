package com.example.androidmaiden.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


// androidMain
@Composable
actual fun ShowDialogButton(showDialog: Boolean, onToggle: () -> Unit) {
    FilledTonalButton(onClick = onToggle) {
        Icon(
            imageVector = if (showDialog) Icons.AutoMirrored.Filled.Chat else Icons.Filled.Person,
            contentDescription = null
        )
        Spacer(Modifier.width(6.dp))
        Text(if (showDialog) "人物+对话" else "仅人物")
    }
}

@Composable
actual fun SwitchLayoutButton(enabled: Boolean, onSwitch: () -> Unit) {
    FilledTonalButton(onClick = onSwitch, enabled = enabled) {
        Icon(Icons.Filled.SwapHoriz, contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text("切换布局")
    }
}

@Composable
actual fun DevButton() {
    OutlinedButton(onClick = { /* TODO */ }) {
        Icon(Icons.Filled.Build, contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text("开发中")
    }
}
