# File System Processing & FileClassifyPage Analysis

## 1. Module Responsibilities & Architecture

The project follows a **Modular Monolith + Clean Architecture** approach with **MVVM** in the UI layer.

### Core Components:
- **UI Layer (`FileClassifyPage`, `FilesListPage`, `FileAnalysisPage`)**:
    - `FileClassifyPage`: Entry point showing storage summary, search, file categories, and analysis blocks.
    - `FilesListPage`: Detailed viewer for categories with List/Grid modes, sorting, and file operations.
    - `FileAnalysisPage`: Deep architectural explorer offering simulated Android 13 OS and real device storage analysis.

- **Presentation Layer (`PersistentFileViewModel`, `FileScannerViewModel`)**:
    - Bridges Repository and UI.
    - Handles data transformation (Classification, Stats calculation).
    - Manages search state, path stack, and UI view modes.

- **Domain/Data Layer (`FileRepository`, `FileMetadataDao`, `AndroidFolderExplainer`)**:
    - `FileRepository`: Single source of truth orchestrating DB and Scanner.
    - `AndroidFolderExplainer`: Domain service providing Android 13 architecture context for directories.
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
- ✅ **Storage Analysis**: Large files, recent files, and folder type distribution.
- ✅ **File Operations**: Delete and Rename integrated with UI and physical storage.
- ✅ **File Analysis Integration**: Synced "Real Data" and "Simulated Data" modes in `FileAnalysisPage` with `AndroidFolderExplainer` architecture insights.

### Remaining Tasks & Shortcomings:
- [ ] **Move Operation**: Infrastructure is ready, but UI needs a folder picker or "Move Mode".
- [ ] **Deep Scan Optimization**: Two-pass scanning (Stats first, heavy tags later).
- [ ] **Search Interaction**: Search results should open preview or navigate to location.
- [ ] **Thumbnail Caching**: Improve performance of large list scrolling with optimized thumbnails.
- [ ] **Batch Operations**: Allow selecting multiple files for delete/move.

## 3. Improvement Suggestions

1. **Background Syncing**: Move `syncRoot` to `WorkManager` for periodic background updates.
2. **Unified Navigation**: Clicking a file in search results should trigger the `FilePreviewOverlay` or navigate to its parent category.
3. **Permission Handling**: Add a more graceful "Permission Denied" UI state.
4. **MIME Type Mapping**: Use `mimeType` from `FileMetadata` for more accurate icon and viewer selection.
