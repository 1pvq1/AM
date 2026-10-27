package com.example.androidmaiden.platform

import android.content.*
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import androidx.documentfile.provider.DocumentFile
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.domain.service.ModelScanner
import kotlinx.coroutines.*
import java.io.File

/**
 * Android platform implementation of ModelScanner.
 * Scans public downloads, AI Edge Gallery target folders, and recursive SAF directory trees (`ACTION_OPEN_DOCUMENT_TREE`)
 * for local on-device models in non-public or app-specific directories.
 *
 * @property context Android application context.
 */
class AndroidModelScanner(
    private val context: Context
) : ModelScanner {

    private val supportedExtensions = setOf("task", "bin", "tflite", "gguf", "onnx", "litert")

    override suspend fun scanForModels(): List<LocalAiModel> = withContext(Dispatchers.IO) {
        val discoveredModels = mutableListOf<LocalAiModel>()

        // 1. Scan public Downloads directory and AI Edge Gallery subfolders
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (downloadsDir.exists() && downloadsDir.isDirectory) {
            val targetDirs = listOf(
                downloadsDir,
                File(downloadsDir, "AI_Edge_Gallery"),
                File(downloadsDir, "MediaPipe"),
                File(downloadsDir, "LLM"),
                File(downloadsDir, "AI_Models")
            )

            for (dir in targetDirs) {
                if (dir.exists() && dir.isDirectory) {
                    val files = dir.listFiles() ?: continue
                    for (file in files) {
                        if (file.isFile && supportedExtensions.contains(file.extension.lowercase())) {
                            val sourceTag = if (dir.name.contains("AI_Edge_Gallery", ignoreCase = true)) {
                                "AI Edge Gallery"
                            } else {
                                "Downloads"
                            }
                            discoveredModels.add(createModelFromFile(file, sourceTag))
                        }
                    }
                }
            }
        }

        discoveredModels
    }

    override suspend fun scanFolderTree(treeUriString: String): List<LocalAiModel> = withContext(Dispatchers.IO) {
        val discoveredModels = mutableListOf<LocalAiModel>()
        try {
            val treeUri = Uri.parse(treeUriString)
            
            // Take persistable URI permission for SAF folder tree
            try {
                context.contentResolver.takePersistableUriPermission(
                    treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Ignore if permission already granted or not persistable
            }

            val rootDoc = DocumentFile.fromTreeUri(context, treeUri)
            if (rootDoc != null && rootDoc.exists()) {
                val folderName = rootDoc.name ?: "SAF Folder"
                traverseDocumentTree(rootDoc, discoveredModels, "SAF: $folderName")
            }
        } catch (e: Exception) {
            // Log or handle scan error gracefully
        }
        discoveredModels
    }

    /**
     * Recursively traverses a DocumentFile tree for supported AI model files.
     */
    private fun traverseDocumentTree(docFile: DocumentFile, results: MutableList<LocalAiModel>, sourceTag: String) {
        try {
            if (docFile.isDirectory) {
                docFile.listFiles().forEach { child ->
                    traverseDocumentTree(child, results, sourceTag)
                }
            } else if (docFile.isFile) {
                val fileName = docFile.name ?: ""
                val extension = fileName.substringAfterLast('.', "").lowercase()
                if (supportedExtensions.contains(extension)) {
                    val uri = docFile.uri
                    val model = LocalAiModel(
                        id = uri.toString().hashCode().toString(),
                        name = fileName,
                        path = uri.toString(),
                        uriString = uri.toString(),
                        format = mapExtensionToFormat(extension),
                        sizeBytes = docFile.length(),
                        sourceTag = sourceTag,
                        isAvailable = docFile.exists()
                    )
                    results.add(model)
                }
            }
        } catch (e: Exception) {
            // Skip unreadable files/folders
        }
    }

    override suspend fun parseModelFromUri(uriString: String, sourceTag: String): Result<LocalAiModel> = withContext(Dispatchers.IO) {
        try {
            val uri = Uri.parse(uriString)
            
            // Take persistent URI permission if granted via SAF
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Ignore if URI does not support persistable permissions
            }

            if (uri.scheme == "file") {
                val file = File(uri.path ?: uriString)
                if (file.exists()) {
                    return@withContext Result.success(createModelFromFile(file, sourceTag))
                }
            }

            // Query display name and size from ContentResolver for content:// URIs
            var displayName = "Imported_Model.task"
            var sizeBytes = 0L

            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex != -1) displayName = cursor.getString(nameIndex) ?: displayName
                    if (sizeIndex != -1) sizeBytes = cursor.getLong(sizeIndex)
                }
            }

            val extension = displayName.substringAfterLast('.', "task").lowercase()
            val format = mapExtensionToFormat(extension)

            // Cache or bridge stream to local cache file for native C++ engine access
            val cachedFile = File(context.cacheDir, "cached_$displayName")
            if (!cachedFile.exists() || cachedFile.length() == 0L) {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    cachedFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            val model = LocalAiModel(
                id = uriString.hashCode().toString(),
                name = displayName,
                path = cachedFile.absolutePath,
                uriString = uriString,
                format = format,
                sizeBytes = if (sizeBytes > 0) sizeBytes else cachedFile.length(),
                sourceTag = sourceTag,
                isAvailable = cachedFile.exists()
            )

            Result.success(model)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createModelFromFile(file: File, sourceTag: String): LocalAiModel {
        val extension = file.extension.lowercase()
        return LocalAiModel(
            id = file.absolutePath.hashCode().toString(),
            name = file.name,
            path = file.absolutePath,
            uriString = file.absolutePath,
            format = mapExtensionToFormat(extension),
            sizeBytes = file.length(),
            sourceTag = sourceTag,
            isAvailable = file.exists()
        )
    }

    private fun mapExtensionToFormat(extension: String): ModelFileFormat {
        return when (extension) {
            "task" -> ModelFileFormat.TASK
            "bin" -> ModelFileFormat.BIN
            "tflite" -> ModelFileFormat.TFLITE
            "gguf" -> ModelFileFormat.GGUF
            "onnx" -> ModelFileFormat.ONNX
            "litert" -> ModelFileFormat.LITERT
            else -> ModelFileFormat.UNKNOWN
        }
    }
}
