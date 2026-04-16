package com.example.androidmaiden.screens.fileSystem.analyze

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.androidmaiden.model.FileSysNode

/**
 * Dialog for confirming the deletion of a file or folder.
 *
 * @param node The node to be deleted.
 * @param onConfirm Callback when deletion is confirmed.
 * @param onDismiss Callback to dismiss the dialog.
 */
@Composable
fun DeleteConfirmDialog(
    node: FileSysNode,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Item") },
        text = { Text("Are you sure you want to delete '${node.name}'? This cannot be undone.") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Dialog for renaming a file or folder.
 *
 * @param node The node to be renamed.
 * @param onConfirm Callback with the new name when renaming is confirmed.
 * @param onDismiss Callback to dismiss the dialog.
 */
@Composable
fun RenameDialog(
    node: FileSysNode,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(node.name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Item") },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("New Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newName.isNotBlank() && newName != node.name) {
                        onConfirm(newName)
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text("Rename")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
