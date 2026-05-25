# General Coding Guidelines & Naming Conventions

This document establishes the standards for code organization, naming, and architectural patterns in the AndroidMaiden project.

## 1. Architectural Layers

We follow a clean architecture approach, separated into three primary layers:

### Presentation Layer
- **Components:** ViewModels, Composable Screens, Pages, and Components.
- **Naming:** 
    - Screens/Pages: `[Feature]Screen` or `[Feature]Page`.
    - ViewModels: `[Feature]ViewModel`.
    - UI State: `[Feature]UiState` (usually a data class).
- **Guidelines:**
    - Use the **Stateless/Stateful** pattern for Composables (see `UI_Architecture_and_Naming.md`).
    - ViewModels should expose state via `StateFlow` and handle interactions via functions.

### Domain Layer
- **Components:** Use Cases, Business Models, and Repository Interfaces.
- **Naming:**
    - Models: Plain names (e.g., `ChatMessage`, `User`).
    - Repository Interfaces: `[Feature]Repository`.
    - Use Cases: `[Action][Feature]UseCase` (e.g., `SendMessageUseCase`).
- **Guidelines:** This layer should be pure Kotlin and independent of any platform-specific libraries.

### Data Layer
- **Components:** Repository Implementations, Mappers, Data Sources (API, Local DB).
- **Naming:**
    - Repository Impl: `[Feature]RepositoryImpl` (if interface exists in Domain).
    - DAOs: `[Entity]Dao`.
    - DTOs: `[Entity]Dto` or `[Action]Response`.
- **Guidelines:** Handle data mapping between network/database models and domain models here.

---

## 2. Naming Conventions

### Files and Classes
- Use `PascalCase` for class names and file names containing a single primary class.
- Use `camelCase` for function names and variables.
- Extension files: `[Target]Ext.kt` (e.g., `StringExt.kt`).

### Package Structure
- `data`: Repositories, DAOs, Networking.
- `domain`: Use Cases, Models, Repository Interfaces.
- `presentation`:
    - `ui`: `screens`, `components`, `theme`.
    - `viewmodel`: Platform-specific and shared ViewModels.
- `platform`: `expect/actual` definitions.
- `di`: Koin modules.

---

## 3. Kotlin Multiplatform (KMP) Patterns

### Expect/Actual
- Use `expect/actual` only when a platform-specific API is absolutely necessary.
- Prefer providing platform-specific implementations via interfaces and DI (Koin) if possible.
- Naming: Keep the same name for `expect` and `actual` declarations.

### Dependency Injection (Koin)
- Define modules in `commonMain` where possible.
- Use `koinInject()` for repositories in Composables (preferably in the Stateful wrapper).
- Use `koinViewModel()` for injecting ViewModels.

---

## 4. Concurrency (Coroutines & Flows)

- **State:** Prefer `StateFlow` over `SharedFlow` for UI state to ensure the latest value is always available.
- **Scopes:** 
    - Use `viewModelScope` in ViewModels.
    - Use `rememberCoroutineScope()` in Composables for UI-triggered events (like showing a Snackbar).
- **Flow Collection:** Use `collectAsState()` or `collectAsStateWithLifecycle()` (on Android) in Composables.

---

## 5. UI Guidelines

See [UI Architecture and Naming](UI_Architecture_and_Naming.md) for detailed conventions on:
- Stateless/Stateful Composables.
- "Content" views.
- Component hierarchy (Screen -> Group -> Component).
