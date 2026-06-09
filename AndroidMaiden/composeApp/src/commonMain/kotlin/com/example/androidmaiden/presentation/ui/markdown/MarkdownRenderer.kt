package com.example.androidmaiden.presentation.ui.markdown

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
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
    // In preview mode, we avoid koinInject to prevent "KoinApplication has not been started" errors.
    // We also avoid the Mature engine in Preview because it currently causes a NoSuchMethodError
    // in LayoutLib due to binary compatibility issues with internal Compose APIs.
    val isPreview = LocalInspectionMode.current
    var useMatureEngine by remember { mutableStateOf(!isPreview) }

    if (!isPreview) {
        val settingsRepository = koinInject<SettingsRepository>()
        LaunchedEffect(settingsRepository) {
            useMatureEngine = settingsRepository.useMatureMarkdown.first()
        }
    }
    
    val toyEngine = remember { ToyMarkdownEngine() }
    val matureEngine = remember { MatureMarkdownEngine() }

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
