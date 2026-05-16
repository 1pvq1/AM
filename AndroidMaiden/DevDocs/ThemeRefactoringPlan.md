# Theme Refactoring Plan (Optimized for Extensibility)

## 1. Problem Analysis
The current theme implementation is fragmented and lacks a clear path for future expansion:
- **Hardcoded Values**: Many UI components use local `Color(...)` and `RoundedCornerShape(...)`, making it impossible to switch themes globally.
- **Single-Theme Design**: The current structure doesn't easily support adding new visual styles (e.g., "Dark Mode Plus", "Cyberpunk", or "Minimalist").
- **Architectural Violation**: Theme-related enums (like `ThemeMode`) and `CompositionLocal`s are scattered in screens or top-level files.

## 2. Refactoring Plan

### Phase 1: Core Foundation & Abstraction (`ui.theme.core`)
1.  **Define Theme Contracts**: Create `ThemeContract.kt` to house:
    - `ThemeMode` (Light, Dark, System).
    - `AppThemeType` (Enum to select between themes: `DEFAULT`, `CUSTOM_ONE`, etc.).
    - `ButtonDisplayStyle` and its `LocalButtonDisplayStyle`.
2.  **Shared Foundation**:
    - `Type.kt`: Define the typography system (Font families, standard text styles) that remains consistent across themes.
    - `Shape.kt`: Define base shapes and corner radii used globally.

### Phase 2: Default Theme Implementation (`ui.theme.DefaultTheme`)
1.  **Encapsulation**: Group all styles specific to the app's default look here.
2.  **Default Palette**: Create `Color.kt` containing the specific light and dark `ColorScheme` for the "Default" brand.
3.  **Specialized Shapes**: (Optional) If the default theme requires specific shapes different from the core.

### Phase 3: The Theme Dispatcher (`ui.theme.Theme.kt`)
1.  **Unified Entry Point**: Create the `AppTheme` composable.
    - **Dispatching Logic**: Based on an `AppThemeType` (stored in settings), it selects the corresponding color scheme (e.g., `DefaultColorScheme`).
    - **Material3 Integration**: Wraps the content in `MaterialTheme(...)`.
    - **Provider Setup**: Configures `CompositionLocalProvider` for global settings like button display styles.

### Phase 4: Integration & Global Cleanup
1.  **App Level**: Update `App.kt` to use `AppTheme`.
2.  **Component Level**: Systematically replace hardcoded `Color` and `Shape` calls with `MaterialTheme.colorScheme` and `MaterialTheme.shapes`.

## 3. New Directory Structure
```
ui/
└── theme/
    ├── Theme.kt            // Main entry point & theme selection logic (The Dispatcher)
    ├── core/               // Shared logic and abstractions (Theme Agnostic)
    │   ├── ThemeContract.kt // Enums, AppThemeType, CompositionLocals
    │   ├── Type.kt          // Shared Typography
    │   └── Shape.kt         // Base Shape definitions
    ├──  DefaultTheme/       // Specific implementation of the "Default" visual style
    │   └── Color.kt        // Default theme specific colors
    │   └── DefaultTheme.kt // (Optional) Default theme specific logic/shapes
    └── CUSTOM_ONE/         // Specific implementation of a custom visual style
        └── Color.kt        // Custom theme specific colors
        └── CustomTheme.kt  // (Optional) Custom theme specific logic/shapes
```

## 4. Why this structure?
- **High Extensibility**: Adding a new theme is as simple as creating a new folder (e.g., `theme/NeonTheme/`), defining its colors, and adding it to the `AppThemeType` enum.
- **Separation of Concerns**: Visual definitions (colors) are separated from structural definitions (typography/shapes) and logic (dispatcher).
- **Consistency**: Centralizing shared assets in `core/` ensures that even with different colors, the app maintains a consistent "feel" (spacing, text sizes).
- **Maintainability**: Clear separation between "how the theme works" (Dispatcher/Contracts) and "what the theme looks like" (Specific Theme Folders).


## 5. KMP-specific Checks
- No Android APIs in `commonMain`.
- All theme definitions use pure Compose Multiplatform libraries.
- Ensure `ThemeMode` and `AppThemeType` are persistent (e.g., via `AppSettings`).
