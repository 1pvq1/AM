# Project Development Status

##  Progress overview

| Platform | UI Status | LLM Integration | Next Steps |
| :--- | :--- | :--- | :--- |
| 🤖 Android | ✅ Full | ✅ Full | Refine & Enhance |
| 💻 Desktop (JVM) | 🟡 Partial | ❌ Mock | Implement LLM |
| 🍎 iOS | 🟡 Partial | ❌ Mock | Implement LLM |
| 🌐 Web (JS) | 🟡 Partial | ❌ Mock | Implement LLM |
| 🕸️ Web (WasmJs) | 🟡 Partial | ❌ Mock | Implement LLM |

This document outlines the current development progress of the AndroidMaiden project for each supported platform.

## Summary

The project is a Kotlin Multiplatform application with a focus on providing a chat interface to interact with a character powered by a large language model (LLM). The core functionality is being developed in `commonMain` and then specialized for each platform.

## Platform-Specific Progress

### Android

*   **Status:** Most advanced platform.
*   **UI:** Fully implemented using Jetpack Compose. Includes a settings screen for API key and model selection, and a chat screen with two view modes (regular and virtual).
*   **LLM Integration:**  The Android app is fully connected to the Gemini API. Users can enter their API key, select a model, and have a real-time conversation with the character.
*   **Next Steps:** Continue to refine the chat experience and add more advanced features.

### Desktop (JVM)

*   **Status:** Partially implemented.
*   **UI:** The UI is shared with Android via Compose Multiplatform, so the basic layout is in place. However, the LLM integration is a mock implementation.
*   **LLM Integration:**  Currently uses a mock `LlmSettingsViewModel` and `ChatViewModel`, so it does not connect to a real LLM.
*   **Next Steps:** Implement the LLM integration to connect to the Gemini API, similar to the Android version.

### iOS

*   **Status:** Partially implemented.
*   **UI:**  The UI is shared with Android and Desktop, so the basic layout is present.
*   **LLM Integration:**  Similar to the Desktop version, iOS uses a mock implementation for the LLM integration. The `LlmSettingsViewModel` and `ChatViewModel` are dummy classes.
*   **Next Steps:** Implement the LLM integration for iOS, which will likely require a platform-specific implementation to handle the API requests.

### Web (JS and WasmJs)

*   **Status:** Least advanced platforms.
*   **UI:** The UI is shared, but the web-specific adaptations might be needed. The current focus has been on the mobile and desktop versions.
*   **LLM Integration:** Both JS and WasmJs use mock implementations for the `LlmSettingsViewModel` and `ChatViewModel`.
*   **Next Steps:**  Implement the LLM integration for both platforms. This will require platform-specific code to make network requests to the Gemini API from the browser.
