package com.example.androidmaiden.data.repository

import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.domain.repository.LocalModelRepository
import com.example.androidmaiden.domain.service.ModelScanner
import kotlinx.coroutines.flow.*

/**
 * Implementation of LocalModelRepository managing discovered and registered local on-device AI models.
 * Delegates file system scanning, SAF folder tree scanning, and URI parsing to platform-specific ModelScanner implementations.
 *
 * @property modelScanner Platform-specific model scanner.
 * @property settingsRepository Repository for persisting granted folder tree URIs.
 */
class LocalModelRepositoryImpl(
    private val modelScanner: ModelScanner? = null,
    private val settingsRepository: SettingsRepository? = null
) : LocalModelRepository {

    private val _availableModels = MutableStateFlow<List<LocalAiModel>>(emptyList())
    override val availableModels: Flow<List<LocalAiModel>> = _availableModels.asStateFlow()

    private val _activeModel = MutableStateFlow<LocalAiModel?>(null)
    override val activeModel: Flow<LocalAiModel?> = _activeModel.asStateFlow()

    override val grantedFolderUris: Flow<Set<String>> = 
        settingsRepository?.grantedModelFolderUris ?: flowOf(emptySet())

    override suspend fun discoverLocalModels(): List<LocalAiModel> {
        val scanner = modelScanner ?: return emptyList()
        val discoveredList = mutableListOf<LocalAiModel>()

        // 1. Scan public directories
        discoveredList.addAll(scanner.scanForModels())

        // 2. Scan all user-granted SAF folder trees
        val persistedFolderUris = grantedFolderUris.first()
        for (treeUri in persistedFolderUris) {
            val treeModels = scanner.scanFolderTree(treeUri)
            discoveredList.addAll(treeModels)
        }

        // Merge with existing registered models avoiding duplicates
        val currentList = _availableModels.value.toMutableList()
        discoveredList.forEach { model ->
            if (currentList.none { it.path == model.path || it.id == model.id }) {
                currentList.add(model)
            }
        }

        _availableModels.value = currentList
        if (_activeModel.value == null && currentList.isNotEmpty()) {
            _activeModel.value = currentList.first()
        }
        return currentList
    }

    override suspend fun registerModelUri(uriString: String, sourceTag: String): Result<LocalAiModel> {
        val scanner = modelScanner 
            ?: return Result.failure(IllegalStateException("No model scanner registered for this platform"))
            
        return scanner.parseModelFromUri(uriString, sourceTag).onSuccess { model ->
            val currentList = _availableModels.value.toMutableList()
            // Replace if exists, otherwise append
            val existingIndex = currentList.indexOfFirst { it.id == model.id || it.path == model.path }
            if (existingIndex >= 0) {
                currentList[existingIndex] = model
            } else {
                currentList.add(model)
            }
            _availableModels.value = currentList
            _activeModel.value = model
        }
    }

    override suspend fun addGrantedFolderUri(uriString: String) {
        settingsRepository?.addGrantedModelFolderUri(uriString)
        discoverLocalModels()
    }

    override suspend fun removeGrantedFolderUri(uriString: String) {
        settingsRepository?.removeGrantedModelFolderUri(uriString)
        // Re-filter available models
        _availableModels.update { list ->
            list.filterNot { it.uriString.startsWith(uriString) }
        }
    }

    override suspend fun setActiveModel(modelId: String) {
        val found = _availableModels.value.find { it.id == modelId }
        if (found != null) {
            _activeModel.value = found
        }
    }

    override suspend fun unregisterModel(modelId: String) {
        _availableModels.update { list -> list.filterNot { it.id == modelId } }
        if (_activeModel.value?.id == modelId) {
            _activeModel.value = _availableModels.value.firstOrNull()
        }
    }
}
