# File System Processing & FileClassifyPage Analysis

## 1. Module Responsibilities & Architecture

The project follows a **Modular Monolith + Clean Architecture** approach with **MVVM** in the UI layer.

### Core Components:
- **UI Layer (`FileClassifyPage`, `FilesListPage`)**:
    - `FileClassifyPage`: High-level entry point showing file categories (Images, Videos, etc.) and analysis blocks (Large Files, Recent Files).
    - `FilesListPage`: Detailed view for a specific category with support for List/Grid modes and sorting.
    - **Responsibility**: Rendering state from ViewModels, handling user interactions, and navigating between views.

- **Presentation Layer (`PersistentFileViewModel`)**:
    - Bridges the Repository and the UI.
    - Transforms raw `FileMetadata` from the database into `FileCategory` models suitable for display.
    - Manages UI state like `viewMode` and `selectedCategory`.

- **Domain/Data Layer (`FileRepository`, `FileMetadataDao`)**:
    - `FileRepository`: Single source of truth. Coordinates between the database (Room) and the scanner.
    - `FileMetadata`: The core entity representing a file/folder with rich metadata (mimeType, duration, etc.).
    - `FileMetadataDao`: Handles SQL queries for reactive UI updates (Flow).

- **Infrastructure Layer (`AndroidFileSystemScanner`)**:
    - Implements `FileSystemScanner` for Android.
    - Performs incremental scanning by comparing timestamps.
    - Extracts media metadata using `MediaMetadataRetriever`.

## 2. Implementation Level & Progress

### Accomplishments:
- ✅ **Reactive Architecture**: Data flows from DB -> Repository -> ViewModel -> UI via Kotlin Flows.
- ✅ **Incremental Scanning**: Basic implementation to skip unchanged directories is present.
- ✅ **Rich Metadata**: Initial support for Image/Video/Audio metadata extraction.
- ✅ **Adaptive UI**: `FilesListPage` supports different cell types (Images, Videos, APKs) and layout modes.
- ✅ **Persistence**: Use of Room ensures data survives app restarts.

### Shortcomings & Remaining Tasks:
- ⚠️ **Analysis Categories Incomplete**: "Large Files" and "Recent Files" in `PersistentFileViewModel` currently only show headers without real counts or file lists.
- ⚠️ **UI Hardcoding**: `FileClassifyPage` uses fixed index sub-listing which is brittle if `FileTypeUtils` definitions change.
- ⚠️ **Performance**: Large file systems might still see overhead during the initial scan; batching and prioritization could be improved.
- ⚠️ **Feature Boundaries**: Some "Experimental" models (`FileSysNode`) still exist and should be fully migrated to `FileMetadata`.
- ⚠️ **File Operations**: Basic operations (Delete, Rename, Move) are largely missing or not integrated into the UI.

## 3. Improvement Suggestions

1. **Dynamic Sectioning**: Replace hardcoded sub-lists in `FileClassifyPage` with a more robust grouping logic based on `CategoryDef` types.
2. **Analysis Logic Implementation**: Implement the filtering logic for `LargeFiles` (>50MB) and `RecentFiles` (last 7 days) in `PersistentFileViewModel`.
3. **Metadata Extraction Optimization**: Consider a two-pass scan: first pass for file paths/stats, second pass (background) for heavy metadata (thumbnails, media tags).
4. **Scoped Storage Handling**: Ensure robust handling of Android's Scoped Storage permissions for all-file access.
5. **Unified Model**: Remove `FileSysNode` dependencies in favor of `FileMetadata` to reduce complexity.

## 4. Development Plan

### Phase 1: Stability & Completeness (Immediate)
- [ ] Implement analysis logic in `PersistentFileViewModel`.
- [ ] Refactor `FileClassifyPage` to use dynamic grouping.
- [ ] Fix any remaining `FileSysNode` vs `FileMetadata` mapping issues.

### Phase 2: Functional Enhancements
- [ ] Add basic file operations (Delete) with UI confirmation.
- [ ] Improve search functionality within categories.
- [ ] Enhance thumbnail loading (integration with Coil).

### Phase 3: Performance & Polish
- [ ] Optimize scanner with worker-based background processing.
- [ ] Add "Deep Scan" progress indicators per folder.
- [ ] Refine UI transitions and animations.
