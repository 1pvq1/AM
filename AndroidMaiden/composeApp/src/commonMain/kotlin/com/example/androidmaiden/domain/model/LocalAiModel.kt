package com.example.androidmaiden.domain.model

import kotlinx.serialization.*

/**
 * Supported file format for local on-device AI models.
 */
enum class ModelFileFormat {
    TASK,
    BIN,
    TFLITE,
    GGUF,
    ONNX,
    LITERT,
    UNKNOWN
}

/**
 * Domain entity representing an on-device AI model discovered or imported on the local device.
 *
 * @param id Unique identifier for the model (e.g. hash or normalized path).
 * @param name Human-readable name of the model.
 * @param path Absolute file path or content URI string referencing the model.
 * @param uriString Original content URI string if imported via SAF tree or document picker.
 * @param format File format of the model (TASK, BIN, TFLITE, GGUF, ONNX, LITERT).
 * @param sizeBytes File size in bytes.
 * @param sourceTag Origin tag identifying where the model came from (e.g., "AI Edge Gallery", "Downloads", "User Import").
 * @param isAvailable Indicates whether the file is verified and accessible on disk.
 */
@Serializable
data class LocalAiModel(
    val id: String,
    val name: String,
    val path: String,
    val uriString: String = "",
    val format: ModelFileFormat = ModelFileFormat.TASK,
    val sizeBytes: Long = 0L,
    val sourceTag: String = "Downloads",
    val isAvailable: Boolean = true
) {
    /**
     * Formats the model file size into a human-readable string (e.g., "1.8 GB" or "450 MB").
     */
    fun getFormattedSize(): String {
        if (sizeBytes <= 0) return "Unknown size"
        val kb = sizeBytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        return when {
            gb >= 1.0 -> "${(gb * 100).toLong() / 100.0} GB"
            mb >= 1.0 -> "${(mb * 100).toLong() / 100.0} MB"
            else -> "${(kb * 100).toLong() / 100.0} KB"
        }
    }
}
