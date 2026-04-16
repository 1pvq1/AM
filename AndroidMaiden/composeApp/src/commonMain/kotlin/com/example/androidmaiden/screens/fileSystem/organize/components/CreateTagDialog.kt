package com.example.androidmaiden.screens.fileSystem.organize.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.Tag
import com.example.androidmaiden.screens.fileSystem.organize.utils.ColorUtils
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CreateTagDialogPreview() {
    CreateTagDialog(onDismiss = {}, onConfirm = { _, _ -> })
}

@Composable
fun CreateTagDialog(
    tag: Tag? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(tag?.name ?: "") }
    var color by remember { mutableStateOf(tag?.colorHex ?: ColorUtils.tagPalette.first()) }
    val isEdit = tag != null
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Tag" else "Create New Tag") },
        text = {
            Column {
                TextField(
                    value = name, 
                    onValueChange = { name = it }, 
                    label = { Text("Tag Name") },
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                Text("Select Color")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(ColorUtils.tagPalette) { c ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ColorUtils.parseHexColor(c))
                                .clickable { color = c }
                        ) {
                            if (color == c) {
                                Icon(
                                    Icons.Default.Check, 
                                    null, 
                                    tint = Color.White,
                                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, color) }) {
                Text(if (isEdit) "Save" else "Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
