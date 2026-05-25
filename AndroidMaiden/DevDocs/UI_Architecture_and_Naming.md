# UI Architecture and Naming Conventions

This document outlines the architectural patterns and naming conventions used for Jetpack Compose UI components in the AndroidMaiden project. These standards ensure consistency, improve testability, and enable robust Preview support.

## 1. The "Content" Pattern (State Hoisting)

### Why `LlmSettingsContent` was newly defined?
The primary reason for defining a `Content` version of a composable is **State Hoisting**. In the previous implementation, the composable was "Stateful"—it injected its own dependencies (via Koin) and managed its own state internally. This made it impossible to render in Android Studio Previews because the Preview environment does not initialize the Koin container or the full ViewModel lifecycle.

By creating `LlmSettingsContent`, we separated the **UI Logic (Stateless)** from the **Business Logic/Dependency Injection (Stateful)**.

### Definition of "Content"
A `Content` composable is a **Stateless** view. 
- **Role:** Purely responsible for layout and styling.
- **Inputs:** Takes all data as plain parameters (strings, data classes) and all interactions as lambda callbacks (e.g., `onValueChange: (String) -> Unit`).
- **Benefits:** 
    - **Previewable:** Can be easily rendered with dummy data.
    - **Testable:** Can be UI-tested without mocking ViewModels or Repositories.
    - **Reusable:** Can be placed in different contexts (e.g., a bottom sheet vs. a full page).

---

## 2. Component Naming Hierarchy

We follow a specific hierarchy to categorize UI elements based on their scope and responsibility:

### **Screen / Page**
- **Definition:** The top-level destination that occupies the entire viewport (excluding system bars).
- **Naming:** `[Feature]Screen` or `[Feature]Page`.
- **Content:** Usually contains a `Scaffold`, `TopAppBar`, and coordinates multiple `Groups` or `Views`.
- **State:** Usually **Stateful**, collecting state from a ViewModel.
- **Example:** `SettingsScreen`, `AdvancedLlmSettingsPage`.

### **Group**
- **Definition:** A logical collection of related settings or information within a page.
- **Naming:** `[Feature]Group`.
- **Content:** Contains a header/title and multiple individual settings or rows.
- **Example:** `LlmSettingsGroup`, `AppearanceSettingsGroup`.
- **Guideline:** In this project, `SettingsGroup` is a specific component that provides a titled container for settings rows.

### **View / Component**
- **Definition:** A specific, reusable UI element or a distinct section of a page that isn't large enough to be a "Page".
- **Naming:** `[Feature]View` or `[Feature]Component`.
- **Content:** Usually focused on one specific task (e.g., an `ApiKeySetting` or a `UserAvatarView`).
- **Example:** `ModelSelectionSetting`, `ChatPreview`.

---

## 3. Recommended Structure for New Features

When creating a new UI feature, follow this dual-composable pattern:

```kotlin
// 1. STATELESS CONTENT (Stateless)
// Use this for Previews and UI Tests
@Composable
fun FeatureContent(
    uiState: FeatureUiState,
    onAction: () -> Unit
) {
    // UI Layout only
}

// 2. STATEFUL WRAPPER (Stateful)
// Use this for production navigation
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    FeatureContent(
        uiState = state,
        onAction = { viewModel.performAction() }
    )
}
```

## 4. Summary Table

| Term | Scope | State | Dependency Injection |
| :--- | :--- | :--- | :--- |
| **Screen/Page** | Full Screen | Stateful | Yes (ViewModel) |
| **Content** | Screen/Component | Stateless | No |
| **Group** | Section of Page | Variable | Occasional |
| **Component** | Atomic Element | Stateless | No |

## 5. Standard Elements

- **View:** A generic term for any composable, but often used for self-contained UI blocks.
- **Page:** Implies a destination within a multi-step flow or a specific document.
- **Screen:** The highest level of hierarchy in the navigation graph.
- **Section:** Similar to "Group", used to divide long lists of content.
