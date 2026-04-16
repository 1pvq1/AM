package com.example.androidmaiden.screens.fileSystem.organize.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.Tag
import com.example.androidmaiden.screens.fileSystem.organize.utils.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSelectorRow(
    tags: List<Tag>,
    selectedTag: Tag?,
    isEditMode: Boolean,
    onTagSelect: (Tag?) -> Unit,
    onTagDelete: (Tag) -> Unit,
    onTagEdit: (Tag) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            FilterChip(
                selected = selectedTag == null,
                onClick = { onTagSelect(null) },
                label = { Text("All") },
                leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp)) }
            )
        }
        items(tags, key = { it.id }) { tag ->
            InputChip(
                selected = selectedTag == tag,
                onClick = { 
                    if (isEditMode) onTagEdit(tag) else onTagSelect(tag) 
                },
                label = { Text(tag.name) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(ColorUtils.parseHexColor(tag.colorHex))
                    )
                },
                trailingIcon = {
                    if (isEditMode) {
                        IconButton(onClick = { onTagDelete(tag) }, modifier = Modifier.size(18.dp)) {
                            Icon(Icons.Default.Close, null)
                        }
                    } else null
                }
            )
        }
    }
}
