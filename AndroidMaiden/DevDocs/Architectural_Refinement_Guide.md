# Architectural Refinement Guide: Experimental & Core Abstractions

This guide establishes the engineering standards for isolating experimental features and platform-specific implementations within the AndroidMaiden project.

## 1. The "Interface-First" Principle
To ensure stability, all core utilities and experimental features must be defined via interfaces in the **Domain Layer** before being implemented in the **Data** or **Platform** layers.

### 1.1 Time Abstraction (`TimeProvider`)
Do not use `Clock.System.now()` or `Instant.fromEpochMilliseconds()` directly in ViewModels or domain logic.

**Standard Pattern:**
1. **Define** `TimeProvider` interface.
2. **Implement** `RealTimeProvider` (using `kotlinx-datetime`).
3. **Place** both in `core.experimental.time` to localize the `@OptIn` burden.
4. **Provide** via Koin in `CommonModule`.
5. **Usage:** Inject `TimeProvider` into ViewModels to facilitate unit testing and isolate experimental time APIs.

### 1.2 Background Job Management (`BackgroundJobManager`)
Android-specific services like `WorkManager` must be abstracted to allow for future iOS or Desktop background task implementations. Place the interface in `core.experimental.job`.

### 1.3 LLM & AI Services (`LlmService`)
Direct usage of library-specific AI clients (like `com.google.ai.client.generativeai`) must be restricted to the **Platform** or **Data** layers. The UI should only interact with a stable `LlmService` interface defined in the `domain.service`.

### 1.4 Network Client (`HttpClient`)
Do not instantiate `HttpClient` directly in ViewModels. Use a centralized `NetworkManager` in `core.network` or inject the singleton `HttpClient` from Koin.

## 2. Experimental API Wrapper Pattern
When using library features marked with `@Experimental...`, wrap them in internal stable types within the `core.experimental` package.

| Experimental API | Wrapper/Strategy | Path |
| :--- | :--- | :--- |
| `kotlin.time` | Abstract behind `TimeProvider` and `MaidenDuration`. | `core.experimental.time` |
| `androidx.room` (KMP) | Strictly isolate within `data.local`. Repositories must map Entities to stable Domain Models. | `data.local` |
| `androidx.work` | Abbreviate as `BackgroundJobManager`. | `core.experimental.job` |
| `Material3Api` | Standardize common components (TopAppBar, Dialogs). | `core.experimental.ui` |

## 3. Package Structure Evolution

```text
src/commonMain/kotlin/com/example/androidmaiden/
├── core/               <-- Centralized architectural utilities (Stable)
│   ├── arch/           <-- Base classes for MVI/MVVM (MaidenViewModel)
│   ├── experimental/   <-- Safety Bridges for External Experimental APIs
│   │   ├── ui/         <-- Stable wrappers for Experimental Material3
│   │   ├── time/       <-- TimeProvider & Instant formatting
│   │   └── job/        <-- BackgroundJobManager
│   ├── network/        <-- Centralized Ktor configuration & NetworkManager
│   └── util/           <-- Pure Kotlin utilities (Non-experimental)
├── domain/
│   ├── model/          <-- Pure POJOs (e.g., FileItem, TodoItem)
│   ├── repository/     <-- Interfaces for Data persistence
│   └── service/        <-- Interfaces for external services (LlmService)
├── data/
│   ├── local/          <-- Room entities, DAOs, Migrations
│   ├── network/        <-- Ktor implementations, DTOs, API clients
│   ├── repository/     <-- Implementation + Mapping (Entity -> Domain)
│   └── util/           <-- Platform-agnostic data utils
└── presentation/       <-- UI Logic & Composable views
```

## 4. Engineering Checkpoints
- **No Leaky Annotations**: UI code should never need `@OptIn` for a database or time library.
- **Factory Pattern for Utilities**: Use factory methods to create utility instances, allowing for platform-specific optimizations or test-double injections.
- **Mapper Enforcement**: Every data entity MUST have a `.toDomain()` and `Companion.fromDomain()` mapper to prevent database schema changes from breaking the UI.

## 5. Architectural Component Lifecycle: The "Promotion" Path
To maintain a high-quality "Stable Core," components follow a structured migration path.

### 5.1 Stage 1: Experimental Shield (`core/experimental/`)
**Criteria:**
- Uses external libraries in Alpha/Beta (e.g., Room KMP, Material3 Experimental).
- Requires `@OptIn` annotations.
- The internal abstraction is new and needs validation across multiple platforms.
- **Example:** `core/experimental/job` is placed here because KMP background task management is volatile and varies significantly between platforms.

### 5.2 Stage 2: Core Promotion (`core/`)
**Criteria for Promotion:**
- The underlying external library is declared **Stable** by the provider.
- All `@OptIn` requirements are removed.
- The internal interface has remained unchanged through 2+ feature implementations and 2+ platforms.
- **Action:** Move the package from `core/experimental/[feature]` to `core/[feature]`.
