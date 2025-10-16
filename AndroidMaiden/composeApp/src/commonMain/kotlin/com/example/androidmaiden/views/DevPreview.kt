package com.example.androidmaiden.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.views.character.charPreviewItems
import org.jetbrains.compose.ui.tooling.preview.Preview

data class PreviewItem(
    val title: String,
    val content: @Composable () -> Unit
)

/**
 * 开发预览函数，仅用于 Studio Live Edit / Preview
 * 不会在实际应用中调用
 */
@Preview(showBackground = true, widthDp = 800, heightDp = 1000)
@Composable
private fun DEVComponents() {
    val allItems = buildList {
        addAll(charPreviewItems())
    }
    PreviewGrid(allItems)
}

@Composable
fun PreviewGrid(items: List<PreviewItem>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 220.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.title, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(8.dp))
                    item.content()
                }
            }
        }
    }
}
