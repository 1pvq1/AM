# File System Analysis Module Design

## 1. Overview
The File Analysis module is designed to provide users with a deep look into their device's storage. It goes beyond simple file browsing by offering visualization of storage distribution, identification of large/old files, and structural visualization via tree views.

## 2. Architecture (MVVM + Clean Architecture)

### UI Layer (`com.example.androidmaiden.screens.fileSystem.analyze`)
- **FileAnalysisScreen**: The main entry point. Orchestrates the toolbar, status display, and the active view mode (List/Grid/Tree).
- **ViewComponents**: `FileListView`, `FileGridView`, `FileTreeView` - dedicated components for different visualization styles.
- **FileItem**: Reusable component for displaying individual file metadata.
- **PathBreadcrumbs**: Interactive navigation component for jumping between parent folders.

### Presentation Layer (`com.example.androidmaiden.viewModels`)
- **FileScannerViewModel**: 
    - Manages the current path and traversal state.
    - Handles the toggle between Mock and Real data (connected to Room DB).
    - Exposes UI state via `StateFlow` and `MutableState` (loading, error, current nodes).
    - Manages a `pathStack` for hierarchical navigation and breadcrumb support.
    - Integrated with Koin DI for proper lifecycle management.

### Domain/Data Layer
- **FileSysNode**: The unified data model for both mock and real file entries.
- **FileRepository**: Single source of truth for "Real" data, ensuring consistency with the File Classification module.
- **Analysis Engine**: Logic to calculate directory sizes and categorize space usage (In Progress).

## 3. Implementation Progress

| Feature | Status | Note |
| :--- | :--- | :--- |
| Mock Data Support | ✅ Done | Used for rapid UI prototyping. |
| Real Data (DB) | ✅ Done | Reactive connection to Room database via `FileRepository`. |
| View Modes (L/G/T) | ✅ Done | List, Grid, and Tree views fully integrated with navigation. |
| Sorting | ✅ Done | Supports Name, Size, Date (Asc/Desc). |
| Navigation | ✅ Done | Folder drill-down and hierarchical back-stack implemented. |
| Breadcrumbs UI | ✅ Done | Clickable segments for quick navigation. |
| Storage Analysis | 🏗️ In Progress | Missing pie charts / breakdown by type in the current view. |
| File Operations | 🏗️ Partial | Folder navigation works; file preview/details pending integration. |

## 4. Shortcomings & Improvement Plan

### Shortcomings (Addressed)
1. ~~**ViewModel Lifecycle**~~: Now managed via Koin DI.
2. ~~**Navigation**~~: Full folder drill-down supported in all view modes.
3. ~~**Real Data Sync**~~: Connected to the persistent Room "shadow" database.
4. ~~**Breadcrumbs**~~: Implemented interactive path segments.

### Remaining Shortcomings
1. **Tree View Optimization**: Large directories may cause UI stuttering if many nodes are expanded.
2. **Analysis Depth**: Currently a file browser. Needs an "Analysis Dashboard" (pie charts, largest folders).
3. **MIME Type Handling**: Using generic icons; should use specific icons based on file metadata.

### Improvement Plan (Next Steps)
1. **Analysis Dashboard**: Create a new component in the `FileAnalysisScreen` that shows a visual breakdown of the current folder's content (e.g., % of Images, Videos, etc.).
2. **File Actions**: Integrate "Delete", "Rename", and "Preview" (using existing components from `FilesListPage`).
3. **MIME Type Icons**: Improve `FileItem` to show more accurate icons based on file extensions.
4. **Performance**: Implement lazy-loading or pagination for extremely large directories.

# Issues:

## Display aspects:
1. Launch the application and go to the file analysis page. Under simulated data, no 'skeleton' is displayed; instead, you need to switch the data source for it to appear.
~~2. When clicking a certain folder continuously under the tree view, the PathBreadcrumbs will stack repeatedly and cannot display the path correctly.~~
3. The number of folders and the display are incorrect, both are 0, and no actual entries are shown.~~