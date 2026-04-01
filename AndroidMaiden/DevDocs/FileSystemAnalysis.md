# File System Processing & FileClassifyPage Analysis

## 1. Module Responsibilities & Architecture

The project follows a **Modular Monolith + Clean Architecture** approach with **MVVM** in the UI layer.

### Core Components:
- **UI Layer (`FileClassifyPage`, `FilesListPage`)**:
    - `FileClassifyPage`: Entry point showing storage summary, search, file categories, and analysis blocks.
    - `FilesListPage`: Detailed viewer for categories with List/Grid modes, sorting, and file operations.
    - **Responsibility**: UI state observation, user interaction handling, and navigation.

- **Presentation Layer (`PersistentFileViewModel`)**:
    - Bridges Repository and UI.
    - Handles data transformation (Classification, Stats calculation).
    - Manages search state and UI view modes.

- **Domain/Data Layer (`FileRepository`, `FileMetadataDao`)**:
    - `FileRepository`: Single source of truth. Orchestrates DB and Scanner.
    - `FileMetadata`: Core entity with rich metadata.
    - `FileMetadataDao`: Reactive SQL queries (Flow).

- **Infrastructure Layer (`AndroidFileSystemScanner`)**:
    - Android-specific implementation of `FileSystemScanner`.
    - Incremental scanning and media metadata extraction.
    - Physical file operations (Delete, Rename, Move).

## 2. Implementation Level & Progress

### Accomplishments:
- ✅ **Reactive Data Flow**: End-to-end Flow implementation from DB to UI.
- ✅ **Incremental Scanning**: Efficient updates based on directory timestamps.
- ✅ **Rich Metadata**: Video/Audio/Image tag extraction.
- ✅ **Search**: Global real-time search with debounce.
- ✅ **Storage Analysis**: "Large Files" (>50MB) and "Recent Files" (7 days) implemented.
- ✅ **File Operations**: Delete and Rename integrated with UI and physical storage.
- ✅ **Adaptive UI**: Category-specific layouts and density control.

### Remaining Tasks & Shortcomings:
- [ ] **Move Operation**: Infrastructure is ready, but UI needs a folder picker or "Move Mode".
- [ ] **Deep Scan Optimization**: Two-pass scanning (Stats first, heavy tags later).
- [ ] **Search Interaction**: Search results should open preview or navigate to location.
- [ ] **Thumbnail Caching**: Improve performance of large list scrolling with optimized thumbnails.
- [ ] **Batch Operations**: Allow selecting multiple files for delete/move.
- [ ] **File Analysis Integration**: Sync "Real Data" mode in `FileAnalysisScreen` with the `FileRepository` database.

## 3. Improvement Suggestions

1. **Background Syncing**: Move `syncRoot` to `WorkManager` for periodic background updates.
2. **Unified Navigation**: clicking a file in search results should trigger the `FilePreviewOverlay` or navigate to its parent category.
3. **Permission Handling**: Add a more graceful "Permission Denied" UI state.
4. **MIME Type Mapping**: Use `mimeType` from `FileMetadata` for more accurate icon and viewer selection.

## 4. Development Plan

### Phase 1: Interaction & UX (Current)
- [x] Implement search and storage stats.
- [x] Implement file deletion and renaming.
- [ ] Integrate Search Result interactions.
- [ ] Add "Share" and "Copy Path" actions.

### Phase 2: Functional Enhancements
- [ ] Implement a simple "Move to Folder" selection dialog.
- [ ] Add "Favorite" toggle functionality.
- [ ] Implement batch selection mode.

### Phase 3: Performance & Polish
- [ ] Integrate `WorkManager` for background indexing.
- [ ] Refine Coil thumbnail loading for videos.
- [ ] Add "Empty Category" placeholders with illustrations.
