package com.example.androidmaiden.platform

import androidx.compose.runtime.Composable

/**
 * JVM / Desktop actual implementation of rememberFolderPicker.
 */
@Composable
actual fun rememberFolderPicker(onFolderSelected: (String) -> Unit): () -> Unit {
    return {
        // Desktop folder picker stub
    }
}
