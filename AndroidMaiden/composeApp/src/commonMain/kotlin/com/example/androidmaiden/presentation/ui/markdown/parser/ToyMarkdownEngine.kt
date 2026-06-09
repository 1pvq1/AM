package com.example.androidmaiden.presentation.ui.markdown.parser

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.presentation.ui.markdown.model.MarkdownNode

/**
 * A lenient, custom-built Markdown engine designed for fast parsing and streaming resilience.
 */
class ToyMarkdownEngine : MarkdownEngine {

    override fun parse(content: String, isStreaming: Boolean): List<MarkdownNode> {
        val blocks = mutableListOf<MarkdownNode>()
        val lines = content.lines()
        var i = 0
        
        while (i < lines.size) {
            val line = lines[i].trimEnd()
            
            when {
                line.isEmpty() -> { i++ }
                
                line.startsWith("<think>") -> {
                    val contentBuilder = StringBuilder()
                    i++
                    var isComplete = false
                    while (i < lines.size) {
                        if (lines[i].contains("</think>")) {
                            contentBuilder.append(lines[i].substringBefore("</think>"))
                            isComplete = true
                            i++
                            break
                        }
                        contentBuilder.append(lines[i]).append("\n")
                        i++
                    }
                    blocks.add(MarkdownNode.ThinkBlock(contentBuilder.toString().trimEnd(), isComplete))
                }

                line.startsWith("### ") -> {
                    blocks.add(MarkdownNode.Heading(3, parseInline(line.substring(4))))
                    i++
                }
                line.startsWith("## ") -> {
                    blocks.add(MarkdownNode.Heading(2, parseInline(line.substring(3))))
                    i++
                }
                line.startsWith("# ") -> {
                    blocks.add(MarkdownNode.Heading(1, parseInline(line.substring(2))))
                    i++
                }
                
                line.startsWith("```") -> {
                    val language = line.substring(3).trim().ifBlank { null }
                    val code = StringBuilder()
                    i++
                    var isComplete = false
                    while (i < lines.size) {
                        if (lines[i].trim().startsWith("```")) {
                            isComplete = true
                            i++
                            break
                        }
                        code.append(lines[i]).append("\n")
                        i++
                    }
                    // Proactive fix: if streaming and not complete, we assume it's an ongoing block
                    blocks.add(MarkdownNode.CodeBlock(code.toString().trimEnd(), language, isComplete || !isStreaming))
                }
                
                line.startsWith("- ") || line.startsWith("* ") -> {
                    val items = mutableListOf<androidx.compose.ui.text.AnnotatedString>()
                    while (i < lines.size && (lines[i].trim().startsWith("- ") || lines[i].trim().startsWith("* "))) {
                        items.add(parseInline(lines[i].trim().substring(2)))
                        i++
                    }
                    blocks.add(MarkdownNode.ListBlock(items, isOrdered = false))
                }

                line.trim().firstOrNull()?.isDigit() == true && line.contains(". ") -> {
                    val items = mutableListOf<androidx.compose.ui.text.AnnotatedString>()
                    while (i < lines.size && lines[i].trim().firstOrNull()?.isDigit() == true && lines[i].contains(". ")) {
                        val contentPart = lines[i].substring(lines[i].indexOf(". ") + 2)
                        items.add(parseInline(contentPart))
                        i++
                    }
                    blocks.add(MarkdownNode.ListBlock(items, isOrdered = true))
                }
                
                line.startsWith("|") -> {
                    val rows = mutableListOf<List<androidx.compose.ui.text.AnnotatedString>>()
                    while (i < lines.size && lines[i].trim().startsWith("|")) {
                        val row = lines[i].trim().split("|")
                            .filter { it.isNotBlank() }
                            .map { parseInline(it.trim()) }
                        
                        // Skip separator rows like |---|---|
                        if (row.none { it.text.all { c -> c == '-' || c == ':' } }) {
                            rows.add(row)
                        }
                        i++
                    }
                    if (rows.isNotEmpty()) {
                        blocks.add(MarkdownNode.Table(rows))
                    }
                }
                
                line == "---" || line == "***" -> {
                    blocks.add(MarkdownNode.Divider)
                    i++
                }
                
                else -> {
                    blocks.add(MarkdownNode.Paragraph(parseInline(line)))
                    i++
                }
            }
        }
        
        return blocks
    }

    private fun parseInline(text: String) = buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            when {
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
}
