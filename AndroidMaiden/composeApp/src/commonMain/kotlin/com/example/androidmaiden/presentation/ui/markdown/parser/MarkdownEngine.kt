package com.example.androidmaiden.presentation.ui.markdown.parser

import com.example.androidmaiden.presentation.ui.markdown.model.MarkdownNode

/**
 * Interface for Markdown parsing engines.
 */
interface MarkdownEngine {
    /**
     * Parses the raw markdown string into a list of AST nodes.
     * @param content The raw markdown string.
     * @param isStreaming Whether the content is currently being streamed (used for lenient parsing).
     */
    fun parse(content: String, isStreaming: Boolean): List<MarkdownNode>
}
