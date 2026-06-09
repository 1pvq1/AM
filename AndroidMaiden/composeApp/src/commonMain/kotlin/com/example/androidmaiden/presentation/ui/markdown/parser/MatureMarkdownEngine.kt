package com.example.androidmaiden.presentation.ui.markdown.parser

import com.example.androidmaiden.presentation.ui.markdown.model.MarkdownNode

/**
 * A wrapper engine that uses a 3rd party library for high-fidelity markdown rendering.
 * It handles splitting custom tags like <think> before passing the rest to the library.
 */
class MatureMarkdownEngine : MarkdownEngine {
    
    override fun parse(content: String, isStreaming: Boolean): List<MarkdownNode> {
        val nodes = mutableListOf<MarkdownNode>()
        val lines = content.lines()
        var currentMarkdown = StringBuilder()
        var i = 0
        
        while (i < lines.size) {
            val line = lines[i]
            
            if (line.trim().startsWith("<think>")) {
                // Emit accumulated markdown
                if (currentMarkdown.isNotEmpty()) {
                    nodes.add(MarkdownNode.RichText(currentMarkdown.toString().trimEnd()))
                    currentMarkdown = StringBuilder()
                }
                
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
                nodes.add(MarkdownNode.ThinkBlock(contentBuilder.toString().trimEnd(), isComplete))
            } else {
                currentMarkdown.append(line).append("\n")
                i++
            }
        }
        
        if (currentMarkdown.isNotEmpty()) {
            nodes.add(MarkdownNode.RichText(currentMarkdown.toString().trimEnd()))
        }
        
        return nodes
    }
}
