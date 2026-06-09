package com.example.androidmaiden.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing chat sessions and messages.
 */
@Dao
interface ChatDao {

    @Query("SELECT * FROM ChatSession ORDER BY isPinned DESC, lastMessageAt DESC")
    fun getAllSessions(): Flow<List<ChatSession>>

    @Query("SELECT * FROM ChatSession WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: String): ChatSession?

    @Upsert
    suspend fun upsertSession(session: ChatSession)

    @Query("DELETE FROM ChatSession WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query("SELECT * FROM ChatMessageEntity WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessageEntity>>

    @Insert
    suspend fun insertMessage(message: ChatMessageEntity)
    
    @Query("DELETE FROM ChatMessageEntity WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)

    @Transaction
    suspend fun saveMessageAndRefreshSession(message: ChatMessageEntity, title: String? = null) {
        val existingSession = getSessionById(message.sessionId)
        val session = existingSession ?: ChatSession(
            id = message.sessionId,
            title = title ?: message.message.take(20),
            lastMessageAt = message.timestamp,
        )
        upsertSession(session.copy(lastMessageAt = message.timestamp))
        insertMessage(message)
    }
}
