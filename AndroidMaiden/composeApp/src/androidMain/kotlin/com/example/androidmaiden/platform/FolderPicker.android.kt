package com.example.androidmaiden.platform

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

/**
 * Android actual implementation of rememberFolderPicker using ActivityResultContracts.OpenDocumentTree.
 */
@Composable
actual fun rememberFolderPicker(onFolderSelected: (String) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            onFolderSelected(uri.toString())
        }
    }
    return {
        try {
            launcher.launch(null)
        } catch (e: Exception) {
            // Handle launch exception if activity context is unavailable
        }
    }
}
