package com.example.androidmaiden.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.screens.pages.ChatMessage
import com.example.androidmaiden.screens.pages.ChatViewMode
import com.example.androidmaiden.screens.pages.Sender

import com.example.androidmaiden.data.ChatSender
import com.example.androidmaiden.data.LlmProvider
import com.example.androidmaiden.data.LlmProviderType
import com.example.androidmaiden.data.LlmService
import com.example.androidmaiden.data.SettingsHolder
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CharacterInteractionViewModel(private val llmService: LlmService? = null) : BaseViewModel() {
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

    private val _chatHistory = mutableStateListOf<ChatMessage>()
    val chatHistory: List<ChatMessage> get() = _chatHistory

    val availableProviders: List<LlmProvider>
        get() = listOf(
            LlmProvider("gemini", "Gemini (Online)", LlmProviderType.GEMINI),
            LlmProvider("local", "LM Studio (Local)", LlmProviderType.LOCAL_LM_STUDIO, baseUrl = SettingsHolder.localLlmAddress)
        )

    var selectedProvider by mutableStateOf(
        availableProviders.find { it.id == SettingsHolder.selectedProviderId } ?: availableProviders.first()
    )
        private set

    fun onProviderSelect(provider: LlmProvider) {
        selectedProvider = provider
        SettingsHolder.selectedProviderId = provider.id
        showProviderPicker = false
    }

    fun toggleProviderPicker() {
        showProviderPicker = !showProviderPicker
    }

    fun initChat(initialMessages: List<ChatMessage>) {
        if (_chatHistory.isEmpty()) {
            _chatHistory.addAll(initialMessages)
        }
    }

    fun onViewModeChange(newMode: ChatViewMode) {
        viewMode = newMode
    }

    fun onFullScreenToggle(onToggle: (Boolean) -> Unit) {
        isFullScreen = !isFullScreen
        onToggle(isFullScreen)
    }

    fun onTextChanged(newText: String) {
        text = newText
    }

    fun sendMessage() {
        if (text.isNotBlank() && !isSending) {
            val userText = text
            _chatHistory.add(ChatMessage(userText, Sender.USER))
            text = ""
            isSending = true

            viewModelScope.launch {
                // Prepare history for LLM
                val historyForLlm = _chatHistory.map {
                    com.example.androidmaiden.data.ChatMessage(
                        it.message,
                        if (it.sender == Sender.USER) ChatSender.USER else ChatSender.CHARACTER
                    )
                }

                // Add placeholder for character response
                val responseIndex = _chatHistory.size
                _chatHistory.add(ChatMessage("", Sender.CHARACTER))

                llmService?.generateContentStream(userText, historyForLlm)
                    ?.onStart { /* Handle start */ }
                    ?.onCompletion { isSending = false }
                    ?.collect { chunk ->
                        val currentMsg = _chatHistory[responseIndex]
                        _chatHistory[responseIndex] = currentMsg.copy(message = currentMsg.message + chunk)
                    } ?: run {
                    // Fallback if service is null
                    _chatHistory[responseIndex] = ChatMessage("LLM Service not initialized", Sender.CHARACTER)
                    isSending = false
                }
            }
        }
    }
}
