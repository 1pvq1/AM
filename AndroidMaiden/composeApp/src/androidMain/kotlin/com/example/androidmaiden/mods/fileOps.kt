package com.example.androidmaiden.mods

import android.media.MediaMetadataRetriever
import com.example.androidmaiden.model.*
import com.example.androidmaiden.utils.formatDuration
import java.io.File
import kotlin.io.path.extension
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

actual fun listFiles(path: String): List<FileSysNode> {
    // For development and testing, you can limit the traversal depth.
    // Set to Int.MAX_VALUE for full traversal.
    val maxDepth = 3 // Example: Limit to 1 level deep

    return listFileViaKotlinIO(path, maxDepth)
}

/**
 * Extracts basic metadata for media files to show in the Analysis tree.
 */
private fun extractQuickMetadata(file: File): String {
    val extension = file.extension.lowercase()
    val isVideo = extension in listOf("mp4", "mkv", "mov", "avi", "wmv", "webm")
    val isAudio = extension in listOf("mp3", "wav", "flac", "aac", "m4a", "ogg")
    
    if (!isVideo && !isAudio) return if (file.isDirectory) "Folder" else "File"
    
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(file.absolutePath)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
        val durationText = duration?.let { formatDuration(it) } ?: ""
        
        if (isVideo) {
            val w = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val h = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            if (w != null && h != null) "${w}x${h} • $durationText" else durationText
        } else {
            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            if (artist != null) "$artist • $durationText" else durationText
        }
    } catch (e: Exception) {
        "Media File"
    } finally {
        retriever.release()
    }
}

/**
 * Traverses files using java.io.File and a recursive approach.
 *
 * This implementation is straightforward but can suffer from `StackOverflowError` on very deep directory structures.
 *
 * @param path The starting path for traversal.
 * @param maxDepth The maximum depth of directories to traverse.
 */
@OptIn(ExperimentalTime::class)
fun listFileViaJavaIO(path: String, maxDepth: Int = Int.MAX_VALUE): List<FileSysNode> {
    val root = File(path)
    if (!root.exists() || !root.isDirectory) return emptyList()

    fun traverse(file: File, currentDepth: Int): FileSysNode {
        val children = if (file.isDirectory && currentDepth < maxDepth) {
            file.listFiles()?.map { traverse(it, currentDepth + 1) } ?: emptyList()
        } else {
            emptyList()
        }

        return FileSysNode(
            name = file.name,
            size = if (file.isFile) file.length() else null,
            lastModified = file.lastModified().takeIf { it > 0 } ?: Clock.System.now()
                .toEpochMilliseconds(),
            nodeType = if (file.isDirectory) NodeType.FOLDER else NodeType.FILE,
            folderType = if (file.isDirectory) FolderType.FOLDER else FolderType.OTHER,
            description = extractQuickMetadata(file),
            dataSource = DataSource.REAL,
            children = children,
            path = file.absolutePath
        )
    }

    return root.listFiles()?.map { traverse(it, 1) } ?: emptyList()
}

/**
 * Traverses files using kotlin.io extensions, which is more idiomatic and robust in Kotlin.
 *
 * This implementation uses `File.walk()` with a `BOTTOM_UP` direction, which is non-recursive and avoids stack overflow issues.
 * It builds the file tree from the leaves up to the root.
 *
 * @param path The starting path for traversal.
 * @param maxDepth The maximum depth of directories to traverse.
 */
@OptIn(ExperimentalTime::class)
fun listFileViaKotlinIO(path: String, maxDepth: Int = Int.MAX_VALUE): List<FileSysNode> {
    val root = File(path)
    if (!root.exists() || !root.isDirectory) return emptyList()

    val nodeCache = mutableMapOf<String, FileSysNode>()

    root.walk(FileWalkDirection.BOTTOM_UP)
        .maxDepth(maxDepth)
        .forEach { file ->
            val children = if (file.isDirectory) {
                file.listFiles()?.mapNotNull { child -> nodeCache[child.absolutePath] } ?: emptyList()
            } else {
                emptyList()
            }

            val node = FileSysNode(
                name = file.name,
                size = if (file.isFile) file.length() else null,
                lastModified = file.lastModified().takeIf { it > 0 } ?: Clock.System.now()
                    .toEpochMilliseconds(),
                nodeType = if (file.isDirectory) NodeType.FOLDER else NodeType.FILE,
                folderType = if (file.isDirectory) FolderType.FOLDER else FolderType.OTHER,
                description = extractQuickMetadata(file),
                dataSource = DataSource.REAL,
                children = children,
                path = file.absolutePath
            )
            nodeCache[file.absolutePath] = node
        }

    return root.listFiles()?.mapNotNull { nodeCache[it.absolutePath] } ?: emptyList()
}
