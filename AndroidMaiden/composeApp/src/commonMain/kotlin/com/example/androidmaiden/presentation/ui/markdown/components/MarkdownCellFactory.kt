package com.example.androidmaiden.presentation.ui.markdown.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.presentation.ui.markdown.components.cells.ThinkCollapseCell
import com.example.androidmaiden.presentation.ui.markdown.model.MarkdownNode

/**
 * Factory for creating UI cells based on Markdown AST nodes.
 */
@Composable
fun MarkdownCell(node: MarkdownNode, isMature: Boolean = false) {
    when (node) {
        is MarkdownNode.RichText -> {
            if (isMature) {
                // If it crashes, we use a simple text display
                Box(modifier = Modifier.fillMaxWidth()) {
                    com.mikepenz.markdown.m3.Markdown(
                        content = node.content,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                ParagraphCell(androidx.compose.ui.text.AnnotatedString(node.content))
            }
        }
        is MarkdownNode.Paragraph -> ParagraphCell(node.content)
        is MarkdownNode.Heading -> HeadingCell(node.content, node.level)
        is MarkdownNode.CodeBlock -> CodeBlockCell(node.code, node.language, node.isComplete)
        is MarkdownNode.ThinkBlock -> ThinkCollapseCell(node.content, node.isComplete)
        is MarkdownNode.ListBlock -> ListCell(node.items, node.isOrdered)
        is MarkdownNode.Table -> TableCell(node.rows)
        MarkdownNode.Divider -> HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
private fun ParagraphCell(content: AnnotatedString) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = 24.sp
    )
}

@Composable
private fun HeadingCell(content: AnnotatedString, level: Int) {
    val style = when (level) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        else -> MaterialTheme.typography.titleLarge
    }
    Text(
        text = content,
        style = style,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun CodeBlockCell(code: String, language: String?, isComplete: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        if (!language.isNullOrBlank()) {
            Text(
                text = language.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Text(
            text = if (isComplete) code else "$code ▌",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ListCell(items: List<AnnotatedString>, isOrdered: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEachIndexed { index, item ->
            Row {
                Text(
                    text = if (isOrdered) "${index + 1}. " else "• ",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun TableCell(rows: List<List<AnnotatedString>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                row.forEach { cell ->
                    Box(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cell,
                            style = if (index == 0) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
                            fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            if (index < (rows.size - 1)) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}
