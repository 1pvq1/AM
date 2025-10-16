package com.example.androidmaiden.ui

import androidx.compose.runtime.Composable

// commonMain
@Composable
expect fun ShowDialogButton(
    showDialog: Boolean,
    onToggle: () -> Unit
)

@Composable
expect fun SwitchLayoutButton(
    enabled: Boolean,
    onSwitch: () -> Unit
)

@Composable
expect fun DevButton()
