package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.domain.model.ChatMessage as DomainChatMessage
import com.example.androidmaiden.domain.model.ChatViewMode
import com.example.androidmaiden.domain.model.Sender
import com.example.androidmaiden.data.network.*
import com.example.androidmaiden.data.repository.*
import com.example.androidmaiden.data.local.ChatSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.time.Clock


/**
 * ViewModel for managing the character interaction screen state and logic.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterInteractionViewModel(
    private val settingsRepository: SettingsRepository,
    private val chatRepository: ChatRepository,
    private val llmService: LlmService? = null
) : BaseViewModel() {
    
    var currentSessionId by mutableStateOf("default-session")
        private set
    
    private val _allSessions = mutableStateListOf<ChatSession>()
    val allSessions: List<ChatSession> get() = _allSessions

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

    var availableModels = mutableStateListOf<String>()
        private set

    var selectedModel by mutableStateOf<String?>(null)
        private set

    var tokenUsage by mutableStateOf(0f) // 0.0 to 1.0
        private set

    var showModelPicker by mutableStateOf(false)
        private set

    init {
        // Load all sessions
        chatRepository.getAllSessions()
            .onEach { sessions ->
                _allSessions.clear()
                _allSessions.addAll(sessions)
            }
            .launchIn(viewModelScope)

        // Reactively load messages when currentSessionId changes
        snapshotFlow { currentSessionId }
            .flatMapLatest { sessionId ->
                chatRepository.getMessagesForSession(sessionId)
            }
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
            selectedProvider = availableProviders.find { it.id == selectedId } ?: availableProviders.firstOrNull()
            
            // Fetch models for the selected provider
            viewModelScope.launch {
                val models = llmService?.getAvailableModels() ?: emptyList()
                
                // Get stored configs for this provider
                val configsJson = settingsRepository.modelConfigsJson.first()
                val json = Json { ignoreUnknownKeys = true }
                val storedConfigs: List<ModelConfig> = configsJson?.let {
                    try {
                        json.decodeFromString<List<ModelConfig>>(it)
                    } catch (e: Exception) {
                        emptyList()
                    }
                } ?: emptyList()

                // Filter and sort available models based on configs
                val providerConfigs = storedConfigs.filter { it.providerId == selectedId }
                
                val filteredModels = if (providerConfigs.isNotEmpty()) {
                    providerConfigs
                        .filter { it.isEnabled }
                        .sortedBy { it.order }
                        .map { it.id }
                        .filter { models.contains(it) } // Only those actually available on the server
                } else {
                    models
                }

                availableModels.clear()
                availableModels.addAll(filteredModels)
                
                // Set selected model from repository or default
                val savedModel = settingsRepository.selectedModel.first()
                selectedModel = if (filteredModels.contains(savedModel)) savedModel else filteredModels.firstOrNull()
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Selects a new model and saves the preference.
     */
    fun onModelSelect(model: String) {
        viewModelScope.launch {
            settingsRepository.saveSelectedModel(model)
            selectedModel = model
            showModelPicker = false
        }
    }

    fun toggleModelPicker() {
        showModelPicker = !showModelPicker
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
     * Starts a new chat session.
     */
    @OptIn(ExperimentalTime::class)
    fun createNewSession() {
        val newId = "session_${Clock.System.now().toEpochMilliseconds()}"
        currentSessionId = newId
    }

    /**
     * Selects an existing chat session.
     */
    fun onSessionSelect(session: ChatSession) {
        currentSessionId = session.id
    }

    /**
     * Deletes a chat session.
     */
    fun deleteSession(session: ChatSession) {
        viewModelScope.launch {
            chatRepository.deleteSession(session.id)
            if (currentSessionId == session.id) {
                currentSessionId = _allSessions.firstOrNull()?.id ?: "default-session"
            }
        }
    }

    /**
     * Renames a chat session.
     */
    fun renameSession(session: ChatSession, newTitle: String) {
        viewModelScope.launch {
            // Placeholder: In a real app, update the session title in ChatRepository
            println("Renaming session ${session.id} to $newTitle")
        }
    }

    /**
     * Pins or unpins a chat session.
     */
    fun togglePinSession(session: ChatSession) {
        viewModelScope.launch {
            // Placeholder: In a real app, update the pinned status in ChatRepository
            println("Toggling pin for session ${session.id}")
        }
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
                        // Update UI in real-time
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
