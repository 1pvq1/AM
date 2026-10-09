package com.example.androidmaiden.domain.model

import androidx.compose.ui.graphics.vector.*

/**
 * Interface representing a subcategory type within a main file category.
 */
interface FileSubcategory {
    val id: String
    val displayName: String
}

/**
 * Subcategories for Audio files.
 */
enum class AudioSubcategory(override val id: String, override val displayName: String) : FileSubcategory {
    ALL("audio_all", "All Audio"),
    MUSIC("audio_music", "Music"),
    RECORDINGS("audio_recordings", "Recordings & Voice"),
    RINGTONES("audio_ringtones", "Ringtones & Alarms"),
    PODCASTS_AUDIOBOOKS("audio_podcasts", "Audiobooks & Podcasts")
}

/**
 * Subcategories for Document files.
 */
enum class DocumentSubcategory(override val id: String, override val displayName: String) : FileSubcategory {
    ALL("doc_all", "All Documents"),
    PDF("doc_pdf", "PDFs"),
    WORD("doc_word", "Word & Text"),
    SPREADSHEET("doc_excel", "Spreadsheets"),
    PRESENTATION("doc_ppt", "Presentations"),
    TEXT_CODE("doc_code", "Code & Markup")
}

/**
 * Subcategories for Archive files.
 */
enum class ArchiveSubcategory(override val id: String, override val displayName: String) : FileSubcategory {
    ALL("archive_all", "All Archives"),
    ZIP("archive_zip", "ZIP Archives"),
    RAR_7Z("archive_rar_7z", "RAR & 7Z"),
    TAR_GZ("archive_tar_gz", "TAR & GZ"),
    DISK_IMAGE("archive_disk_image", "ISOs & Images")
}

/**
 * Subcategories for APK files.
 */
enum class ApkSubcategory(override val id: String, override val displayName: String) : FileSubcategory {
    ALL("apk_all", "All APKs"),
    APK("apk_installer", "Installer APKs"),
    AAB_SPLIT("apk_bundle", "App Bundles & Splits")
}

/**
 * Subcategories for Image files.
 */
enum class ImageSubcategory(override val id: String, override val displayName: String) : FileSubcategory {
    ALL("image_all", "All Images"),
    PHOTOS("image_photos", "Camera Photos"),
    SCREENSHOTS("image_screenshots", "Screenshots"),
    GIFS_ANIMATIONS("image_gifs", "GIFs & Animations"),
    DOWNLOADS("image_downloads", "Downloaded Images")
}

/**
 * Subcategories for Video files.
 */
enum class VideoSubcategory(override val id: String, override val displayName: String) : FileSubcategory {
    ALL("video_all", "All Videos"),
    CAMERA_VIDEOS("video_camera", "Camera Videos"),
    SCREEN_RECORDINGS("video_screen", "Screen Recordings"),
    MOVIES_SHOWS("video_movies", "Movies & TV")
}

/**
 * Metadata info holder for a subcategory including stats and optional icon.
 *
 * @param id Unique identifier of the subcategory.
 * @param displayName Human-readable title of the subcategory.
 * @param count Total number of files belonging to this subcategory.
 * @param totalSizeMb Aggregated size in megabytes.
 * @param icon Optional ImageVector for visual representation.
 */
data class SubcategoryInfo(
    val id: String,
    val displayName: String,
    val count: Int = 0,
    val totalSizeMb: Long = 0L,
    val icon: ImageVector? = null
)
