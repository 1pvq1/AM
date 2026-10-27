package com.example.androidmaiden.domain.repository

import com.example.androidmaiden.domain.model.LocalAiModel
import kotlinx.coroutines.flow.*

/**
 * Repository interface for managing local on-device AI models.
 * Responsible for model discovery, SAF tree scanning, directory permissions, and tracking the active model.
 */
interface LocalModelRepository {

    /**
     * Observable stream of all registered or discovered local AI models.
     */
    val availableModels: Flow<List<LocalAiModel>>

    /**
     * Observable stream of the currently selected active local model.
     */
    val activeModel: Flow<LocalAiModel?>

    /**
     * Observable stream of user-granted directory tree URIs for local model scanning.
     */
    val grantedFolderUris: Flow<Set<String>>

    /**
     * Scans public directories and user-granted SAF folder trees for available model files.
     * Reuses already downloaded models to prevent redundant downloads and save device storage.
     *
     * @return List of newly discovered local AI models.
     */
    suspend fun discoverLocalModels(): List<LocalAiModel>

    /**
     * Registers a local model from a user-selected Content URI or file path.
     *
     * @param uriString URI string or file path selected by user via document picker or SAF.
     * @param sourceTag Identifier indicating the source app or origin (default: "User Import").
     * @return Result containing the registered LocalAiModel or failure error.
     */
    suspend fun registerModelUri(uriString: String, sourceTag: String = "User Import"): Result<LocalAiModel>

    /**
     * Persists and scans a user-granted directory tree URI (via ACTION_OPEN_DOCUMENT_TREE).
     *
     * @param uriString SAF directory tree URI string.
     */
    suspend fun addGrantedFolderUri(uriString: String)

    /**
     * Removes a user-granted directory tree URI from persistent registry and unregisters associated models.
     *
     * @param uriString SAF directory tree URI string to remove.
     */
    suspend fun removeGrantedFolderUri(uriString: String)

    /**
     * Sets the specified model as the active local model for on-device inference.
     *
     * @param modelId Unique ID of the model to activate.
     */
    suspend fun setActiveModel(modelId: String)

    /**
     * Removes a local model registration from the repository.
     *
     * @param modelId Unique ID of the model to unregister.
     */
    suspend fun unregisterModel(modelId: String)
}
