package com.example.androidmaiden.domain.service

import com.example.androidmaiden.domain.model.LocalAiModel

/**
 * Platform scanner abstraction for locating local model files in device storage and SAF folder trees.
 */
interface ModelScanner {

    /**
     * Scans public device storage (including Downloads/ and AI Edge Gallery directories) for model files.
     *
     * @return List of discovered model files.
     */
    suspend fun scanForModels(): List<LocalAiModel>

    /**
     * Recursively scans a user-granted SAF directory tree URI (via ACTION_OPEN_DOCUMENT_TREE).
     *
     * @param treeUriString SAF directory tree Content URI string.
     * @return List of discovered model files in the folder tree.
     */
    suspend fun scanFolderTree(treeUriString: String): List<LocalAiModel>

    /**
     * Parses and registers a model file from a Content URI or file path.
     *
     * @param uriString Content URI string or absolute file path.
     * @param sourceTag Identifier for origin app or user import tag.
     * @return Parsed LocalAiModel or failure result.
     */
    suspend fun parseModelFromUri(uriString: String, sourceTag: String = "User Import"): Result<LocalAiModel>
}
