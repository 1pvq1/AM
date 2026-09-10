package com.example.androidmaiden.domain.model

/**
 * Domain-level representation of a file tag.
 */
data class Tag(
    val id: Long = 0,
    val name: String,
    val colorHex: String
)

/**
 * Domain-level representation of a file along with its associated tags.
 */
data class FileWithTags(
    val file: FileItem,
    val tags: List<Tag>
)
