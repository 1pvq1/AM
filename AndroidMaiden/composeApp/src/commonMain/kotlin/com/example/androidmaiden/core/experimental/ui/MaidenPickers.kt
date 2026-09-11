package com.example.androidmaiden.core.experimental.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

/**
 * A stable wrapper for the experimental Material3 DatePickerDialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaidenDatePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    shape: androidx.compose.ui.graphics.Shape = DatePickerDefaults.shape,
    tonalElevation: androidx.compose.ui.unit.Dp = DatePickerDefaults.TonalElevation,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable ColumnScope.() -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        shape = shape,
        tonalElevation = tonalElevation,
        colors = colors,
        properties = properties,
        content = content
    )
}

/**
 * A standardized TimePickerDialog since Material3 does not provide one out of the box.
 */
@Composable
fun MaidenTimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        modifier = modifier,
        text = { content() }
    )
}
