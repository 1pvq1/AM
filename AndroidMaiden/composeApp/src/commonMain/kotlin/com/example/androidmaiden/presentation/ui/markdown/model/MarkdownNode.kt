package com.example.androidmaiden.presentation.ui.markdown.model

import androidx.compose.ui.text.AnnotatedString

/**
 * Sealed interface representing a node in the Markdown Abstract Syntax Tree (AST).
 */
sealed interface MarkdownNode {
    /**
     * Represents a paragraph of text, potentially containing inline styles.
     */
    data class Paragraph(val content: AnnotatedString) : MarkdownNode

    /**
     * Represents a heading (H1-H6).
     */
    data class Heading(val level: Int, val content: AnnotatedString) : MarkdownNode

    /**
     * Represents a code block.
     */
    data class CodeBlock(val code: String, val language: String?, val isComplete: Boolean = true) : MarkdownNode

    /**
     * Represents a reasoning/thought block (specific to LLMs).
     */
    data class ThinkBlock(val content: String, val isComplete: Boolean = true) : MarkdownNode

    /**
     * Represents a block of markdown text to be rendered with rich formatting.
     */
    data class RichText(val content: String) : MarkdownNode

    /**
     * Represents a bullet point or numbered list.
     */
    data class ListBlock(val items: List<AnnotatedString>, val isOrdered: Boolean = false) : MarkdownNode

    /**
     * Represents a table.
     */
    data class Table(val rows: List<List<AnnotatedString>>) : MarkdownNode

    /**
     * Represents a horizontal divider.
     */
    object Divider : MarkdownNode
}
