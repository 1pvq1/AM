package com.example.androidmaiden.domain.model

/**
 * Data model for a single chat message.
 */
data class ChatMessage(val message: String, val sender: Sender)

/**
 * Enum representing the sender of a chat message.
 */
enum class Sender { USER, CHARACTER }

/**
 * Enum representing different view modes for the character interaction.
 */
enum class ChatViewMode { REGULAR, VIRTUAL }
