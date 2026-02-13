package com.example.androidmaiden.utils

import com.example.androidmaiden.model.FileNode
import com.example.androidmaiden.model.NodeType
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Flatten a file tree into a list of all files.
 */
fun FileNode.flatten(): List<FileNode> {
    if (!isFolder) return listOf(this)
    return children.flatMap { it.flatten() }
}

/**
 * Group files by their general type: image, video, doc, apk, etc.
 */
fun FileNode.groupByExtension(): Map<String, List<FileNode>> {
    return flatten()
        .filter { !it.isFolder }
        .groupBy { it.extensionGroup() }
}

fun FileNode.countByType(): Map<String, Int> {
    return groupByExtension().mapValues { it.value.size }
}

fun FileNode.totalSizeByType(): Map<String, Long> {
    return groupByExtension().mapValues { list ->
        list.value.sumOf { it.size ?: 0L }
    }
}

/**
 * Get extension grouping key
 */
fun FileNode.extensionGroup(): String {
    val name = this.name.lowercase()
    return when {
        name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif") -> "Images"
        name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mkv") -> "Videos"
        name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx") -> "Documents"
        name.endsWith(".apk") -> "APKs"
        else -> "Others"
    }
}

/**
 * Get large files over a threshold size (in MB)
 */
fun FileNode.getLargeFiles(thresholdMb: Int = 100): List<FileNode> {
    val bytes = thresholdMb * 1024 * 1024
    return flatten().filter { !it.isFolder && (it.size ?: 0L) >= bytes }
}

/**
 * Get recently modified files (last X milliseconds)
 */
@OptIn(ExperimentalTime::class)
fun FileNode.getRecentFiles(withinMillis: Long): List<FileNode> {
//    val now = System.currentTimeMillis()
    val now = Clock.System.now().toEpochMilliseconds()
    return flatten().filter {
        val modified = it.lastModified ?: return@filter false
        !it.isFolder && (now - modified) <= withinMillis
    }
}