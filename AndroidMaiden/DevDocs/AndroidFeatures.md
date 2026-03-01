# Android Features

This document describes the functionalities available on the Android side of the AndroidMaiden project.

## Core Features

### 1. Chat Interface
- **Character Interaction:** Users can interact with a character powered by a large language model (LLM).
- **View Modes:** The chat screen supports two view modes:
    - **Regular View:** Standard chat bubbles.
    - **Virtual View:** A more immersive interaction mode.
- **LLM Integration:** Fully connected to the Gemini API, allowing for real-time conversations.
- **Settings:** Users can configure their API key and select different models (e.g., Gemini 1.5 Pro, Gemini 1.5 Flash).

### 2. File Management
- **File Explorer:** A functional file explorer that allows users to navigate the device's file system.
- **File Classification:** Categorizes files into different types (Images, Videos, Music, Documents, etc.).
- **File Analysis:** Provides detailed information about files, such as size, last modified date, and path.
- **Permissions:** Handles Android storage permissions (e.g., `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`) to ensure safe and authorized access to files.
- **Syncing:** Includes a `FileSyncManager` for potential file synchronization tasks.

### 3. Task Management (Todo)
- **Basic CRUD:** Users can add, complete, and delete tasks.
- **Task List:** A clear and organized list of tasks.

### 4. System & Hardware
- **Hardware Management:** Ability to identify, maintain, and configure device hardware (under development).
- **Customization:** Users can customize the interface style and theme.

## Platform-Specific Implementations

### Android-Specific Modules (`androidMain`)
- **`fileOps.kt`:** Contains platform-specific implementations for file system operations, using both `java.io` and `kotlin.io` for robustness.
- **`Permissions.kt`:** Manages Android runtime permissions.
- **`FileSyncManager.kt`:** Handles file synchronization logic specific to the Android platform.
- **`MainActivity.kt`:** The main entry point for the Android application, setting up the Compose UI and dependency injection.

## Technical Details

- **UI Framework:** Jetpack Compose for building native Android UIs.
- **Concurrency:** Kotlin Coroutines for asynchronous tasks.
- **Dependency Injection:** Koin (implied by the `di` package).
- **Architecture:** MVVM (Model-View-ViewModel) pattern, with ViewModels managing screen state and logic.
