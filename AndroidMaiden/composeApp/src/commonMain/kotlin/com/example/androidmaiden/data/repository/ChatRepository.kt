package com.example.androidmaiden.data.repository

import com.example.androidmaiden.data.local.*
import com.example.androidmaiden.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Repository for managing chat history and sessions.
 */
class ChatRepository(private val chatDao: ChatDao) {

    /**
     * Returns a stream of all chat sessions.
     */
    fun getAllSessions(): Flow<List<ChatSession>> = chatDao.getAllSessions()

    /**
     * Returns a stream of messages for a specific session.
     */
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesForSession(sessionId).map { entities ->
            entities.map { entity ->
                ChatMessage(
                    message = entity.message,
                    sender = if (entity.sender == "USER") Sender.USER else Sender.CHARACTER
                )
            }
        }
    }

    /**
     * Saves a new message to the local database.
     */
    @OptIn(ExperimentalTime::class)
    suspend fun saveMessage(sessionId: String, message: String, sender: Sender) {
        val entity = ChatMessageEntity(
            sessionId = sessionId,
            message = message,
            sender = if (sender == Sender.USER) "USER" else "CHARACTER",
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        chatDao.saveMessageAndRefreshSession(entity)
    }

    /**
     * Deletes a chat session and all its messages.
     */
    suspend fun deleteSession(sessionId: String) {
        chatDao.deleteSession(sessionId)
    }

    /**
     * Renames a chat session.
     */
    suspend fun renameSession(sessionId: String, newTitle: String) {
        chatDao.getSessionById(sessionId)?.let {
            chatDao.upsertSession(it.copy(title = newTitle))
        }
    }

    /**
     * Pins or unpins a chat session.
     */
    suspend fun togglePinSession(sessionId: String) {
        chatDao.getSessionById(sessionId)?.let {
            chatDao.upsertSession(it.copy(isPinned = !it.isPinned))
        }
    }
}
