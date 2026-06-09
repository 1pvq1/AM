# Markdown Rendering System - Q&A

Following the analysis of the `MarkdownRenderingSystem.md` template, I have identified several architectural points to adapt. Below are the questions regarding the implementation details.

---

### Q1: "Think" Block Visualization
**Question**: Many modern models (like DeepSeek) use `<think>...</think>` tags for internal reasoning. How should these be visually represented in the chat?
*   **Option A**: A collapsible section (like a spoiler) with a label "Reasoning...".
*   **Option B**: A distinct background color (e.g., light gray) with an icon, but always visible.
*   **Option C**: Only show a "View Reasoning" button that opens a dialog.

### Q2: Mature Engine Integration
**Question**: The reference plan suggests hot-swapping between a "Toy" engine (custom regex) and a "Mature" engine (3rd party library). 
*   Do you want me to integrate a 3rd party KMP-compatible library (like `multiplatform-markdown`) now as the default "Mature" engine? 
*   Or should I focus on perfecting the "Toy" engine for now to keep the project light?

### Q3: Hot-Swapping UI Location
**Question**: If we implement the hot-swapping logic, where should the toggle reside?
*   **Option A**: In the existing **Settings > LLM** screen.
*   **Option B**: In a new **Developer Options** hidden menu (e.g., accessed by tapping the version number 5 times).

### Q4: Streaming Logic in UI
**Question**: While the LLM is streaming, should the UI attempt to fix "broken" markdown (e.g., unclosed code blocks or unfinished tables) in real-time?
*   The reference plan suggests a `StreamingSanitizer`. Should this be a priority to avoid layout "jumping" during streaming?

---
*Please provide your answers/preferences in the corresponding implementation plan or by replying to this document.*

## Answers
- A1: A
- A2: Yes, integrate a 3rd party KMP-compatible library as the default "Mature" engine, 'toy' will only undergo proactive updates and improvements when necessary.
- A3：A
- A4: yes