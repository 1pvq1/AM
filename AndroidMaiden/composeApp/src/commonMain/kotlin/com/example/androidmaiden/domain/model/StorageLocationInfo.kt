package com.example.androidmaiden.domain.model

/**
 * Information describing a detected storage space (Internal Storage vs External SD Card).
 *
 * @param name Human-readable storage name (e.g. "Primary Internal Storage").
 * @param path Root path of the storage volume.
 * @param isExternalRemovable True if this volume is a removable SD Card or USB drive.
 * @param totalBytes Total capacity of the storage volume in bytes.
 * @param usedBytes Used space in bytes.
 * @param freeBytes Free/available space in bytes.
 */
data class StorageLocationInfo(
    val name: String,
    val path: String,
    val isExternalRemovable: Boolean = false,
    val totalBytes: Long = 0L,
    val usedBytes: Long = 0L,
    val freeBytes: Long = 0L
) {
    /**
     * Percentage of used space relative to total capacity (0.0 to 100.0).
     */
    val usedPercentage: Float
        get() = if (totalBytes > 0) (usedBytes.toFloat() / totalBytes.toFloat()) * 100f else 0f
}
