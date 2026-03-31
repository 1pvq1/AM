package com.example.androidmaiden.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.androidmaiden.screens.pages.ChatMessage
import com.example.androidmaiden.screens.pages.ChatViewMode
import com.example.androidmaiden.screens.pages.Sender

class CharacterInteractionViewModel : BaseViewModel() {
    var viewMode by mutableStateOf(ChatViewMode.REGULAR)
        private set

    var isFullScreen by mutableStateOf(false)
        private set

    var text by mutableStateOf("")
        private set

    private val _chatHistory = mutableStateListOf<ChatMessage>()
    val chatHistory: List<ChatMessage> get() = _chatHistory

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
        if (text.isNotBlank()) {
            _chatHistory.add(ChatMessage(text, Sender.USER))
            text = ""
            // TODO: Implement character response logic
        }
    }
}
