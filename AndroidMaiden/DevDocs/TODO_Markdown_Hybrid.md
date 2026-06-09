# TODO: Hybrid Markdown Rendering System

## Goal
Implement a high-performance, stable Markdown system that uses a mature AST parser but renders using native Compose components.

## Technical Strategy
1.  **AST Parser**: Integrate `commonmark-java` (Atlassian) or a KMP-equivalent AST parser.
    - This library will *only* be responsible for parsing the raw string into a tree of nodes (e.g., `TextNode`, `CodeBlockNode`).
2.  **Custom Renderer**: Write a native Compose renderer that iterates through the generated AST.
    - Map each AST node type directly to a custom Composable (e.g., `MarkdownCodeBlock()`, `MarkdownTable()`).
    - This gives us full control over styling and prevents binary compatibility crashes from 3rd-party UI libraries.
3.  **LLM Support**: Ensure the "splitter" layer still handles `<think>` tags and streaming sanitization before or during AST generation.

## Status
- [x] Initial Research
- [ ] Dependency Integration (commonmark-java)
- [ ] Node mapping logic
- [ ] Native Compose cell implementation
- [ ] Regression testing with streaming content
