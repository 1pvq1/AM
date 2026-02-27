package com.example.androidmaiden.utils

import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.utils.FileTypeUtils.getExtensionType
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Flatten a file tree into a list of all files.
 */
fun FileSysNode.flatten(): List<FileSysNode> {
    if (!isFolder) return listOf(this)
    return children.flatMap { it.flatten() }
}

/**
 * Group files by their general type: image, video, doc, apk, etc.
 */
fun FileSysNode.groupByExtension(): Map<String, List<FileSysNode>> {
    return flatten()
        .filter { !it.isFolder }
        .groupBy { it.extensionGroup() }
}

fun FileSysNode.countByType(): Map<String, Int> {
    return groupByExtension().mapValues { it.value.size }
}

fun FileSysNode.totalSizeByType(): Map<String, Long> {
    return groupByExtension().mapValues { list ->
        list.value.sumOf { it.size ?: 0L }
    }
}

/**
 * Get extension grouping key
 */
fun FileSysNode.extensionGroup(): String = getExtensionType(this.name)


//fun FileSysNode.extensionGroup(): String {
//    val name = this.name.lowercase()
//    return when {
//        // Images
//        name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif") ||
//            name.endsWith(".webp") || name.endsWith(".bmp") || name.endsWith(".heic") || name.endsWith(".heif") -> "Images"
//
//        // Videos
//        name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mkv") || name.endsWith(".mov") ||
//            name.endsWith(".wmv") || name.endsWith(".webm") || name.endsWith(".m4v") -> "Videos"
//
//        // Audio
//        name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".flac") || name.endsWith(".aac") ||
//            name.endsWith(".m4a") || name.endsWith(".ogg") || name.endsWith(".opus") -> "Audio"
//
//        // Documents
//        name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx") || name.endsWith(".xls") ||
//            name.endsWith(".xlsx") || name.endsWith(".ppt") || name.endsWith(".pptx") || name.endsWith(".txt") ||
//            name.endsWith(".md") || name.endsWith(".rtf") -> "Documents"
//
//        // Application packages
//        name.endsWith(".apk") || name.endsWith(".aab") -> "APKs"
//
//        // Archives
//        name.endsWith(".zip") || name.endsWith(".rar") || name.endsWith(".7z") || name.endsWith(".tar") ||
//            name.endsWith(".gz") || name.endsWith(".bz2") || name.endsWith(".xz") -> "Archives"
//
//        else -> "Others"
//    }
//}

/**
 * Get large files over a threshold size (in MB)
 */
fun FileSysNode.getLargeFiles(thresholdMb: Int = 100): List<FileSysNode> {
    val bytes = thresholdMb * 1024 * 1024
    return flatten().filter { !it.isFolder && (it.size ?: 0L) >= bytes }
}

/**
 * Get recently modified files (last X milliseconds)
 */
@OptIn(ExperimentalTime::class)
fun FileSysNode.getRecentFiles(withinMillis: Long): List<FileSysNode> {
//    val now = System.currentTimeMillis()
    val now = Clock.System.now().toEpochMilliseconds()
    return flatten().filter {
        val modified = it.lastModified ?: return@filter false
        !it.isFolder && (now - modified) <= withinMillis
    }
}