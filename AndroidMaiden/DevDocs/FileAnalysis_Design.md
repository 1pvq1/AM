# File System Analysis Module Design

## 1. Overview
The File Analysis module provides users and developers with architectural insights into Android's storage layout (Android 13+ specification). Beyond standard file browsing, it offers storage distribution visualization, folder hierarchy traversal, and educational explanations of Android's system partitions, Scoped Storage rules, and application sandboxing.

## 2. Architecture (MVVM + Clean Architecture)

### UI Layer (`com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze`)
- **FileAnalysisPage**: Stateful entry point connecting `FileScannerViewModel` to UI components.
- **FileAnalysisCoordinator**: Adaptive UI layout coordinator.
- **FileAnalysisContent**: Main stateless layout displaying breadcrumbs, view modes, and actions.
- **ViewComponents**: `FileListView`, `FileGridView`, `FileTreeView` - dedicated views for different visualization styles.
- **FileItem**: Reusable list item component displaying metadata, Android OS folder explanations, customizable description modes (Concise/Full/Hidden), and icon styles.
- **PathBreadcrumbs**: Interactive path navigation bar supporting customizable separators (`>`, `/`, `|`, `•`, `→`), visible length limits, and root labels.
- **FileActionSheet**: Bottom sheet displaying file actions and Android OS architecture insights.
- **FileAnalysisToolbar**: Features view mode toggle, sorting, data source toggle (Simulated vs Real), and Hidden Data Filter menu (`Show All`, `Exclude Hidden`, `Hidden Only`).
- **ControlAppearanceSettingsScreen**: Dedicated customization page with options and real-time interactive preview for UI controls and hidden file filter preferences.

### Presentation Layer (`com.example.androidmaiden.presentation.viewmodel`)
- **FileScannerViewModel**: 
    - Manages directory traversal state, view modes, hidden file filter modes (`HiddenFilterMode`), and sorting.
    - Supports seamless switching between Simulated Data Mode (Android 13 OS hierarchy) and Real Device Data Mode (Room DB).
    - Performs path lookup (`findMockNode`) for mock directory navigation.
    - Enriches real device folder nodes using `AndroidFolderExplainer`.
    - Queries real-time `FileProvider` directory listings and populates sub-folder child nodes for accurate file/folder counts.
    - Exposes state flows for loading, errors, stats, path stack, and hidden filter modes.
- **SettingsViewModel**:
    - Exposes `controlAppearanceConfig` state flows and setters for DataStore preferences.

### Domain/Data Layer
- **FileSysNode**: Unified hierarchical node model for file system entries.
- **UiControlSettings**: Domain models (`BreadcrumbSeparator`, `BreadcrumbMaxSegments`, `BreadcrumbRootLabel`, `FileItemDescriptionMode`, `FileItemIconStyle`, `HiddenFilterMode`, `ControlAppearanceConfig`).
- **AndroidFolderExplainer**: Domain service holding Android 13 architecture explanations (Scoped Storage, APEX, Treble, Direct Boot, etc.).
- **FileRepository**: Single source of truth for real device data connected to Room DB (`FileMetadataDao`) and real-time `FileProvider`.
- **SettingsRepository**: DataStore repository persisting theme, LLM, UI control appearance, and hidden file filter configurations.

## 3. Implementation Progress

| Feature | Status | Note |
| :--- | :--- | :--- |
| Mock Data Support | ✅ Done | Simulated Android 13 OS directory hierarchy with full path navigation. |
| Real Data (DB + OS) | ✅ Done | Immediate `FileProvider` OS listing + reactive Room DB connection. |
| Folder & File Counts | ✅ Done | Sub-folder child node resolution for accurate folder and file counts. |
| System Hidden Data Filter | ✅ Done | Supports `Show All`, `Exclude Hidden`, and `Hidden Only` filtering modes. |
| Architecture Explanations | ✅ Done | `AndroidFolderExplainer` provides explanations for both Mock and Real folders. |
| View Modes (L/G/T) | ✅ Done | List, Grid, and Tree views integrated with single/double-click handlers. |
| Sorting | ✅ Done | Supports Name, Size, Date (Asc/Desc). |
| Navigation & Breadcrumbs | ✅ Done | Accurate path stack navigation with customizable separators & length truncation. |
| UI Control Customization | ✅ Done | Dedicated `ControlAppearanceSettingsScreen` with options & live preview. |
| Storage Analysis | ✅ Done | `FolderAnalysisStats` with distribution bar pop-ups reflecting filtered nodes. |
| File Operations | ✅ Done | Folder opening, renaming, and deletion integrated. |

## 4. Shortcomings & Addressed Issues

### Shortcomings (Addressed)
1. ~~**ViewModel Lifecycle**~~: Managed via Koin DI.
2. ~~**Mock Data Navigation**~~: Implemented recursive `findMockNode` search so clicking folders in mock mode drills down correctly.
3. ~~**Folder Architectural Explanations**~~: Integrated `AndroidFolderExplainer` to explain Android 13 directory rules (Scoped Storage, APEX, Treble, etc.).
4. ~~**Breadcrumb Path Accuracy**~~: Synchronized path stack representation across mock and real device modes.
5. ~~**UI Control Customization**~~: Integrated `ControlAppearanceSettingsScreen` and DataStore preferences for `PathBreadcrumbs` and `FileItem`.
6. ~~**Real Device Folder & File Counts**~~: Refined `FileScannerViewModel` and `FileRepository` to query real-time `FileProvider` listings and populate sub-folder children nodes for accurate counts.
7. ~~**System Hidden Data Filtering**~~: Added `HiddenFilterMode` (`SHOW_ALL`, `EXCLUDE_HIDDEN`, `ONLY_HIDDEN`) in toolbar and Settings repository.

### Remaining Shortcomings & Future Plan
1. **Tree View Optimization**: Implement lazy expansion or node virtualisation for ultra-large directories.
2. **Batch File Actions**: Support multi-select deletion or moving.
