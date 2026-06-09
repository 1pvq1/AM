package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.domain.model.ChatMessage as DomainChatMessage
import com.example.androidmaiden.domain.model.ChatViewMode
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.data.network.*
import com.example.androidmaiden.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the character interaction screen state and logic.
 */
class CharacterInteractionViewModel(
    private val settingsRepository: SettingsRepository,
    private val chatRepository: ChatRepository,
    private val llmService: LlmService? = null
) : BaseViewModel() {
    
    private var currentSessionId by mutableStateOf("default-session")
    
    private val _allSessions = mutableStateListOf<com.example.androidmaiden.data.local.ChatSession>()
    val allSessions: List<com.example.androidmaiden.data.local.ChatSession> get() = _allSessions

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

    private val _chatHistory = mutableStateListOf<DomainChatMessage>()
    val chatHistory: List<DomainChatMessage> get() = _chatHistory

    val availableProviders = mutableStateListOf<LlmProvider>()

    var selectedProvider by mutableStateOf<LlmProvider?>(null)
        private set

    init {
        // Load messages from repository
        chatRepository.getMessagesForSession(currentSessionId)
            .onEach { messages ->
                _chatHistory.clear()
                _chatHistory.addAll(messages)
            }
            .launchIn(viewModelScope)

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
    fun initChat(initialMessages: List<DomainChatMessage>) {
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
            text = ""
            isSending = true

            viewModelScope.launch {
                // Save user message
                chatRepository.saveMessage(currentSessionId, userText, Sender.USER)

                // Prepare history for LLM
                val historyForLlm = _chatHistory.map {
                    ChatMessage(
                        it.message,
                        if (it.sender == Sender.USER) ChatSender.USER else ChatSender.CHARACTER
                    )
                }

                var fullResponse = ""
                llmService?.generateContentStream(userText, historyForLlm)
                    ?.onCompletion { 
                        isSending = false
                        // Save full character response
                        if (fullResponse.isNotBlank()) {
                            chatRepository.saveMessage(currentSessionId, fullResponse, Sender.CHARACTER)
                        }
                    }
                    ?.collect { chunk ->
                        fullResponse += chunk
                        // Optionally update UI in real-time if Flow is slow
                        val lastMsg = _chatHistory.lastOrNull()
                        if (lastMsg?.sender == Sender.CHARACTER) {
                             val index = _chatHistory.size - 1
                             _chatHistory[index] = lastMsg.copy(message = lastMsg.message + chunk)
                        } else {
                             _chatHistory.add(DomainChatMessage(chunk, Sender.CHARACTER))
                        }
                    } ?: run {
                    isSending = false
                }
            }
        }
    }
}
