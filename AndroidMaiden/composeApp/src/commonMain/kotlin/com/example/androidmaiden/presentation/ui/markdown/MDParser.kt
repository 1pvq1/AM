package com.example.androidmaiden.presentation.ui.markdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp


/**
 * Internal models for markdown blocks.
 */
sealed class MarkdownBlock {
    data class Paragraph(val text: String) : MarkdownBlock()
    data class CodeBlock(val code: String, val language: String?) : MarkdownBlock()
    data class ListBlock(val items: List<String>) : MarkdownBlock()
    data class Heading(val text: String, val level: Int) : MarkdownBlock()
    data class TableBlock(val rows: List<List<String>>) : MarkdownBlock()
    object Divider : MarkdownBlock()
}

/**
 * Basic markdown block parser.
 */
fun parseMarkdownBlocks(content: String): List<MarkdownBlock> {
    val blocks = mutableListOf<MarkdownBlock>()
    val lines = content.lines()
    var i = 0

    while (i < lines.size) {
        val line = lines[i].trimEnd()

        when {
            line.isEmpty() -> { i++ }

            line.startsWith("### ") -> {
                blocks.add(MarkdownBlock.Heading(line.substring(4), 3))
                i++
            }
            line.startsWith("## ") -> {
                blocks.add(MarkdownBlock.Heading(line.substring(3), 2))
                i++
            }
            line.startsWith("# ") -> {
                blocks.add(MarkdownBlock.Heading(line.substring(2), 1))
                i++
            }

            line.startsWith("```") -> {
                val language = line.substring(3).trim().ifBlank { null }
                val code = StringBuilder()
                i++
                while (i < lines.size && !lines[i].trim().startsWith("```")) {
                    code.append(lines[i]).append("\n")
                    i++
                }
                blocks.add(MarkdownBlock.CodeBlock(code.toString().trimEnd(), language))
                i++
            }

            line.startsWith("- ") || line.startsWith("* ") -> {
                val items = mutableListOf<String>()
                while (i < lines.size && (lines[i].trim().startsWith("- ") || lines[i].trim().startsWith("* "))) {
                    items.add(lines[i].trim().substring(2))
                    i++
                }
                blocks.add(MarkdownBlock.ListBlock(items))
            }

            line.startsWith("|") -> {
                val rows = mutableListOf<List<String>>()
                while (i < lines.size && lines[i].trim().startsWith("|")) {
                    val row = lines[i].trim().split("|")
                        .filter { it.isNotBlank() }
                        .map { it.trim() }

                    // Skip separator rows like |---|---|
                    if (row.none { it.all { c -> c == '-' || c == ':' } }) {
                        rows.add(row)
                    }
                    i++
                }
                if (rows.isNotEmpty()) {
                    blocks.add(MarkdownBlock.TableBlock(rows))
                }
            }

            line == "---" || line == "***" -> {
                blocks.add(MarkdownBlock.Divider)
                i++
            }

            else -> {
                blocks.add(MarkdownBlock.Paragraph(line))
                i++
            }
        }
    }

    return blocks
}

/**
 * Basic inline markdown parser (Bold, Italic, Code).
 */
fun parseInlineMarkdown(text: String) = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        when {
            // Bold **text**
            text.startsWith("**", i) -> {
                val end = text.indexOf("**", i + 2)
                if (end != -1) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text.substring(i + 2, end))
                    }
                    i = end + 2
                } else {
                    append("**")
                    i += 2
                }
            }
            // Italic *text*
            text.startsWith("*", i) -> {
                val end = text.indexOf("*", i + 1)
                if (end != -1) {
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else {
                    append("*")
                    i += 1
                }
            }
            // Inline Code `text`
            text.startsWith("`", i) -> {
                val end = text.indexOf("`", i + 1)
                if (end != -1) {
                    withStyle(SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color.LightGray.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else {
                    append("`")
                    i += 1
                }
            }
            else -> {
                append(text[i])
                i++
            }
        }
    }
}
