package com.example.androidmaiden.presentation.ui.markdown

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.presentation.ui.markdown.components.MarkdownCell
import com.example.androidmaiden.presentation.ui.markdown.parser.MatureMarkdownEngine
import com.example.androidmaiden.presentation.ui.markdown.parser.ToyMarkdownEngine
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

/**
 * A Composable that renders Markdown text using either a Toy or Mature engine.
 * Supports LLM-specific features like "Think" blocks and streaming sanitization.
 */
@Composable
fun MarkdownRenderer(
    content: String,
    isStreaming: Boolean = false,
    modifier: Modifier = Modifier
) {
    val settingsRepository = koinInject<SettingsRepository>()
    var useMatureEngine by remember { mutableStateOf(true) }
    
    val toyEngine = remember { ToyMarkdownEngine() }
    val matureEngine = remember { MatureMarkdownEngine() }

    LaunchedEffect(Unit) {
        useMatureEngine = settingsRepository.useMatureMarkdown.first()
    }

    val engine = if (useMatureEngine) matureEngine else toyEngine
    
    val nodes = remember(content, engine, isStreaming) {
        engine.parse(content, isStreaming)
    }

    SelectionContainer(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            nodes.forEach { node ->
                MarkdownCell(node = node, isMature = useMatureEngine)
            }
        }
    }
}
