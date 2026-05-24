package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.presentation.ui.screens.pages.*
import com.example.androidmaiden.data.network.*
import com.example.androidmaiden.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the character interaction screen state and logic.
 */
class CharacterInteractionViewModel(
    private val settingsRepository: SettingsRepository,
    private val llmService: LlmService? = null
) : BaseViewModel() {
    var viewMode by mutableStateOf(ChatViewMode.REGULAR)
        private set

    var isFullScreen by mutableStateOf(false)
        private set

    var text by mutableStateOf("")
        private set

    var isSending by mutableStateOf(false)
        private set

    var showProviderPicker by mutableStateOf(false)
        private set

    private val _chatHistory = mutableStateListOf<com.example.androidmaiden.presentation.ui.screens.pages.ChatMessage>()
    val chatHistory: List<com.example.androidmaiden.presentation.ui.screens.pages.ChatMessage> get() = _chatHistory

    val availableProviders = mutableStateListOf<LlmProvider>()

    var selectedProvider by mutableStateOf<LlmProvider?>(null)
        private set

    init {
        // Reactively update available providers and selection when settings change
        combine(
            settingsRepository.localLlmAddress,
            settingsRepository.selectedProviderId
        ) { localAddress, selectedId ->
            availableProviders.clear()
            availableProviders.addAll(listOf(
                LlmProvider("gemini", "Gemini (Online)", LlmProviderType.GEMINI),
                LlmProvider("local", "LM Studio (Local)", LlmProviderType.LOCAL_LM_STUDIO, baseUrl = localAddress)
            ))
            selectedProvider = availableProviders.find { it.id == selectedId } ?: availableProviders.first()
        }.launchIn(viewModelScope)
    }

    /**
     * Selects a new LLM provider and saves the preference.
     */
    fun onProviderSelect(provider: LlmProvider) {
        viewModelScope.launch {
            settingsRepository.saveSelectedProviderId(provider.id)
            showProviderPicker = false
        }
    }

    /**
     * Toggles the visibility of the provider picker menu.
     */
    fun toggleProviderPicker() {
        showProviderPicker = !showProviderPicker
    }

    /**
     * Initializes the chat history with starting messages.
     */
    fun initChat(initialMessages: List<com.example.androidmaiden.presentation.ui.screens.pages.ChatMessage>) {
        if (_chatHistory.isEmpty()) {
            _chatHistory.addAll(initialMessages)
        }
    }

    /**
     * Changes the current chat view mode.
     */
    fun onViewModeChange(newMode: ChatViewMode) {
        viewMode = newMode
    }

    /**
     * Toggles full screen mode and notifies the caller.
     */
    fun onFullScreenToggle(onToggle: (Boolean) -> Unit) {
        isFullScreen = !isFullScreen
        onToggle(isFullScreen)
    }

    /**
     * Updates the current message text.
     */
    fun onTextChanged(newText: String) {
        text = newText
    }

    /**
     * Sends the current text as a message and handles the AI response.
     */
    fun sendMessage() {
        if (text.isNotBlank() && !isSending) {
            val userText = text
            _chatHistory.add(com.example.androidmaiden.presentation.ui.screens.pages.ChatMessage(userText, Sender.USER))
            text = ""
            isSending = true

            viewModelScope.launch {
                // Prepare history for LLM
                val historyForLlm = _chatHistory.map {
                    com.example.androidmaiden.data.network.ChatMessage(
                        it.message,
                        if (it.sender == Sender.USER) ChatSender.USER else ChatSender.CHARACTER
                    )
                }

                // Add placeholder for character response
                val responseIndex = _chatHistory.size
                _chatHistory.add(com.example.androidmaiden.presentation.ui.screens.pages.ChatMessage("", Sender.CHARACTER))

                llmService?.generateContentStream(userText, historyForLlm)
                    ?.onStart { /* Handle start */ }
                    ?.onCompletion { isSending = false }
                    ?.collect { chunk ->
                        val currentMsg = _chatHistory[responseIndex]
                        _chatHistory[responseIndex] = currentMsg.copy(message = currentMsg.message + chunk)
                    } ?: run {
                    // Fallback if service is null
                    _chatHistory[responseIndex] = com.example.androidmaiden.presentation.ui.screens.pages.ChatMessage("LLM Service not initialized", Sender.CHARACTER)
                    isSending = false
                }
            }
        }
    }
}
