package com.example.androidmaiden.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.data.local.FileMetadata
import com.example.androidmaiden.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Centrally managed file categories and sub-classification algorithms across the app.
 */
object FileTypeUtils {

    /**
     * Determines the general category (e.g., "Images", "Videos") based on the file extension.
     */
    fun getExtensionType(fileName: String): String {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        return when (ext) {
            // Images
            "jpg", "jpeg", "png", "webp", "gif", "bmp", "heic" -> "Images"

            // Videos
            "mp4", "mkv", "mov", "avi", "wmv", "webm" -> "Videos"

            // Audio
            "mp3", "wav", "flac", "aac", "m4a", "ogg", "amr", "3gp" -> "Audio"

            // Documents
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "md", "json", "xml", "html", "csv" -> "Documents"

            // Application packages
            "apk", "aab", "apks", "xapk" -> "APKs"

            // Archives
            "zip", "rar", "7z", "tar", "gz", "bz2", "xz", "iso", "img" -> "Archives"
            else -> "Other"
        }
    }

    /**
     * Determines the smart Audio subcategory for an audio file.
     *
     * @param file The audio [FileItem] to classify.
     * @return The determined [AudioSubcategory].
     */
    fun getAudioSubcategory(file: FileItem): AudioSubcategory {
        val pathLower = file.path.lowercase()
        val nameLower = file.name.lowercase()
        val ext = file.extension.lowercase()

        // 1. Recordings & Voice Memos
        val isRecording = listOf("record", "voice", "soundrecorder", "call", "memo", "mic", "audiorecord")
            .any { pathLower.contains(it) || nameLower.contains(it) } || ext == "amr" || ext == "3gp"
        if (isRecording) return AudioSubcategory.RECORDINGS

        // 2. Ringtones & Notifications & Alarms
        val isRingtone = listOf("ringtone", "notification", "alarm")
            .any { pathLower.contains(it) } || (file.duration != null && file.duration in 1..29_999L && file.artist.isNullOrBlank())
        if (isRingtone) return AudioSubcategory.RINGTONES

        // 3. Audiobooks & Podcasts
        val isPodcastOrBook = listOf("podcast", "audiobook", "speech", "lecture")
            .any { pathLower.contains(it) } || (file.duration != null && file.duration >= 600_000L)
        if (isPodcastOrBook) return AudioSubcategory.PODCASTS_AUDIOBOOKS

        // 4. Default to Music
        return AudioSubcategory.MUSIC
    }

    /**
     * Determines the smart Document subcategory for a document file.
     *
     * @param file The document [FileItem] to classify.
     * @return The determined [DocumentSubcategory].
     */
    fun getDocumentSubcategory(file: FileItem): DocumentSubcategory {
        return when (file.extension.lowercase()) {
            "pdf" -> DocumentSubcategory.PDF
            "doc", "docx", "rtf", "odt", "pages" -> DocumentSubcategory.WORD
            "xls", "xlsx", "csv", "numbers" -> DocumentSubcategory.SPREADSHEET
            "ppt", "pptx", "key" -> DocumentSubcategory.PRESENTATION
            "txt", "md", "json", "xml", "kt", "java", "py", "html", "css", "sh", "c", "cpp", "js", "ts" -> DocumentSubcategory.TEXT_CODE
            else -> DocumentSubcategory.WORD
        }
    }

    /**
     * Determines the smart Archive subcategory for an archive file.
     *
     * @param file The archive [FileItem] to classify.
     * @return The determined [ArchiveSubcategory].
     */
    fun getArchiveSubcategory(file: FileItem): ArchiveSubcategory {
        return when (file.extension.lowercase()) {
            "zip" -> ArchiveSubcategory.ZIP
            "rar", "7z" -> ArchiveSubcategory.RAR_7Z
            "tar", "gz", "bz2", "xz", "tgz" -> ArchiveSubcategory.TAR_GZ
            "iso", "img", "dmg" -> ArchiveSubcategory.DISK_IMAGE
            else -> ArchiveSubcategory.ZIP
        }
    }

    /**
     * Determines the smart APK subcategory for an app package file.
     *
     * @param file The APK [FileItem] to classify.
     * @return The determined [ApkSubcategory].
     */
    fun getApkSubcategory(file: FileItem): ApkSubcategory {
        return when (file.extension.lowercase()) {
            "aab", "apks", "xapk" -> ApkSubcategory.AAB_SPLIT
            else -> ApkSubcategory.APK
        }
    }

    /**
     * Determines the smart Image subcategory for an image file.
     *
     * @param file The image [FileItem] to classify.
     * @return The determined [ImageSubcategory].
     */
    fun getImageSubcategory(file: FileItem): ImageSubcategory {
        val pathLower = file.path.lowercase()
        return when {
            pathLower.contains("screenshot") -> ImageSubcategory.SCREENSHOTS
            file.extension.equals("gif", ignoreCase = true) -> ImageSubcategory.GIFS_ANIMATIONS
            pathLower.contains("download") -> ImageSubcategory.DOWNLOADS
            pathLower.contains("dcim") || pathLower.contains("camera") -> ImageSubcategory.PHOTOS
            else -> ImageSubcategory.PHOTOS
        }
    }

    /**
     * Determines the smart Video subcategory for a video file.
     *
     * @param file The video [FileItem] to classify.
     * @return The determined [VideoSubcategory].
     */
    fun getVideoSubcategory(file: FileItem): VideoSubcategory {
        val pathLower = file.path.lowercase()
        return when {
            pathLower.contains("screenrecord") || pathLower.contains("screen_record") -> VideoSubcategory.SCREEN_RECORDINGS
            pathLower.contains("dcim") || pathLower.contains("camera") -> VideoSubcategory.CAMERA_VIDEOS
            else -> VideoSubcategory.MOVIES_SHOWS
        }
    }

    /**
     * Calculates subcategory breakdowns (counts, sizes, metadata) for a given main category type.
     *
     * @param categoryType Main category name (e.g. "Audio", "Documents").
     * @param files List of [FileItem] belonging to the category.
     * @return List of [SubcategoryInfo] items for UI rendering.
     */
    fun calculateSubcategoriesForCategory(categoryType: String, files: List<FileItem>): List<SubcategoryInfo> {
        return when (categoryType) {
            "Audio" -> {
                val grouped = files.groupBy { getAudioSubcategory(it) }
                val allInfo = SubcategoryInfo(
                    id = AudioSubcategory.ALL.id,
                    displayName = AudioSubcategory.ALL.displayName,
                    count = files.size,
                    totalSizeMb = files.sumOf { it.size } / (1024 * 1024)
                )
                val subInfos = AudioSubcategory.entries.filter { it != AudioSubcategory.ALL }.map { sub ->
                    val items = grouped[sub] ?: emptyList()
                    SubcategoryInfo(
                        id = sub.id,
                        displayName = sub.displayName,
                        count = items.size,
                        totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
                    )
                }
                listOf(allInfo) + subInfos
            }

            "Documents" -> {
                val grouped = files.groupBy { getDocumentSubcategory(it) }
                val allInfo = SubcategoryInfo(
                    id = DocumentSubcategory.ALL.id,
                    displayName = DocumentSubcategory.ALL.displayName,
                    count = files.size,
                    totalSizeMb = files.sumOf { it.size } / (1024 * 1024)
                )
                val subInfos = DocumentSubcategory.entries.filter { it != DocumentSubcategory.ALL }.map { sub ->
                    val items = grouped[sub] ?: emptyList()
                    SubcategoryInfo(
                        id = sub.id,
                        displayName = sub.displayName,
                        count = items.size,
                        totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
                    )
                }
                listOf(allInfo) + subInfos
            }

            "Archives" -> {
                val grouped = files.groupBy { getArchiveSubcategory(it) }
                val allInfo = SubcategoryInfo(
                    id = ArchiveSubcategory.ALL.id,
                    displayName = ArchiveSubcategory.ALL.displayName,
                    count = files.size,
                    totalSizeMb = files.sumOf { it.size } / (1024 * 1024)
                )
                val subInfos = ArchiveSubcategory.entries.filter { it != ArchiveSubcategory.ALL }.map { sub ->
                    val items = grouped[sub] ?: emptyList()
                    SubcategoryInfo(
                        id = sub.id,
                        displayName = sub.displayName,
                        count = items.size,
                        totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
                    )
                }
                listOf(allInfo) + subInfos
            }

            "APKs" -> {
                val grouped = files.groupBy { getApkSubcategory(it) }
                val allInfo = SubcategoryInfo(
                    id = ApkSubcategory.ALL.id,
                    displayName = ApkSubcategory.ALL.displayName,
                    count = files.size,
                    totalSizeMb = files.sumOf { it.size } / (1024 * 1024)
                )
                val subInfos = ApkSubcategory.entries.filter { it != ApkSubcategory.ALL }.map { sub ->
                    val items = grouped[sub] ?: emptyList()
                    SubcategoryInfo(
                        id = sub.id,
                        displayName = sub.displayName,
                        count = items.size,
                        totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
                    )
                }
                listOf(allInfo) + subInfos
            }

            "Images" -> {
                val grouped = files.groupBy { getImageSubcategory(it) }
                val allInfo = SubcategoryInfo(
                    id = ImageSubcategory.ALL.id,
                    displayName = ImageSubcategory.ALL.displayName,
                    count = files.size,
                    totalSizeMb = files.sumOf { it.size } / (1024 * 1024)
                )
                val subInfos = ImageSubcategory.entries.filter { it != ImageSubcategory.ALL }.map { sub ->
                    val items = grouped[sub] ?: emptyList()
                    SubcategoryInfo(
                        id = sub.id,
                        displayName = sub.displayName,
                        count = items.size,
                        totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
                    )
                }
                listOf(allInfo) + subInfos
            }

            "Videos" -> {
                val grouped = files.groupBy { getVideoSubcategory(it) }
                val allInfo = SubcategoryInfo(
                    id = VideoSubcategory.ALL.id,
                    displayName = VideoSubcategory.ALL.displayName,
                    count = files.size,
                    totalSizeMb = files.sumOf { it.size } / (1024 * 1024)
                )
                val subInfos = VideoSubcategory.entries.filter { it != VideoSubcategory.ALL }.map { sub ->
                    val items = grouped[sub] ?: emptyList()
                    SubcategoryInfo(
                        id = sub.id,
                        displayName = sub.displayName,
                        count = items.size,
                        totalSizeMb = items.sumOf { it.size } / (1024 * 1024)
                    )
                }
                listOf(allInfo) + subInfos
            }

            else -> emptyList()
        }
    }

    /**
     * Filters files belonging to a specific main category by a chosen subcategory ID.
     *
     * @param categoryType Main category name (e.g. "Audio").
     * @param subcategoryId Active subcategory ID string (e.g. "audio_music").
     * @param files List of [FileItem] to filter.
     * @return Filtered list of [FileItem].
     */
    fun filterFilesBySubcategory(categoryType: String, subcategoryId: String?, files: List<FileItem>): List<FileItem> {
        if (subcategoryId.isNullOrBlank() || subcategoryId.endsWith("_all")) return files

        return when (categoryType) {
            "Audio" -> files.filter { getAudioSubcategory(it).id == subcategoryId }
            "Documents" -> files.filter { getDocumentSubcategory(it).id == subcategoryId }
            "Archives" -> files.filter { getArchiveSubcategory(it).id == subcategoryId }
            "APKs" -> files.filter { getApkSubcategory(it).id == subcategoryId }
            "Images" -> files.filter { getImageSubcategory(it).id == subcategoryId }
            "Videos" -> files.filter { getVideoSubcategory(it).id == subcategoryId }
            else -> files
        }
    }

    /**
     * Central source of truth for file category definitions.
     */
    val categoryDefinitions = listOf(
        CategoryDef("Images", Icons.Default.Image, "Images"),
        CategoryDef("Videos", Icons.Default.Videocam, "Videos"),
        CategoryDef("Audio", Icons.Default.MusicNote, "Audio"),
        CategoryDef("Documents", Icons.Default.Description, "Documents"),
        CategoryDef("APKs", Icons.Default.Android, "APKs"),
        CategoryDef("Archives", Icons.Default.Archive, "Archives"),
    )

    /**
     * Specialized categories for analysis (Non-extension based).
     */
    val analysisDefinitions = listOf(
        CategoryDef("Large Files (> 50MB)", Icons.Default.DiscFull, "LargeFiles"),
        CategoryDef("Recent Files (Last 7 Days)", Icons.Default.Schedule, "RecentFiles"),
        CategoryDef("Other", Icons.AutoMirrored.Filled.InsertDriveFile, "Other")
    )
}

/**
 * Internal data class for category mapping with FileSysNode support.
 */
private data class FileCategoryOfFileSysNode(
    val name: String,
    val icon: ImageVector,
    val type: String,
    val count: Int? = null,
    val totalSizeMb: Long? = null,
    val files: List<FileSysNode> = emptyList()
)

/**
 * Extension function to convert FileMetadata to a basic FileSysNode.
 */
fun FileMetadata.toFileNode(): FileSysNode {
    return FileSysNode(
        name = this.name,
        path = this.path,
        nodeType = if (this.isDirectory) NodeType.FOLDER else NodeType.FILE,
        size = this.size,
        lastModified = this.lastModified,
        children = emptyList()
    )
}
