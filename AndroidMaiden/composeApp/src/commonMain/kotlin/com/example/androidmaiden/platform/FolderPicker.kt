package com.example.androidmaiden.platform

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic composable helper to launch a folder picker (e.g. SAF OpenDocumentTree on Android).
 *
 * @param onFolderSelected Callback invoked with the selected directory URI string.
 * @return Action lambda to trigger the picker dialog.
 */
@Composable
expect fun rememberFolderPicker(onFolderSelected: (String) -> Unit): () -> Unit
