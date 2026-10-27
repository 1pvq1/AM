package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.domain.model.LocalAiModel
import com.example.androidmaiden.domain.repository.LocalModelRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI State holding data for discovered and active local AI models, as well as granted SAF folder trees.
 *
 * @param availableModels List of models found on the device.
 * @param activeModel Currently selected model for on-device inference.
 * @param grantedFolders Set of user-granted directory tree URIs.
 * @param isScanning Indicates whether a scan for downloaded models is in progress.
 * @param errorMessage Error message if model registration fails.
 */
data class LocalModelUiState(
    val availableModels: List<LocalAiModel> = emptyList(),
    val activeModel: LocalAiModel? = null,
    val grantedFolders: Set<String> = emptySet(),
    val isScanning: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel managing local on-device model discovery, selection, folder tree registration, and SAF URI import.
 *
 * @property localModelRepository Repository for managing local model state.
 */
class LocalModelViewModel(
    private val localModelRepository: LocalModelRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocalModelUiState())
    val uiState: StateFlow<LocalModelUiState> = _uiState.asStateFlow()

    init {
        observeRepositoryState()
        discoverModels()
    }

    /**
     * Subscribes to available models, active model, and granted folder URIs from the repository.
     */
    private fun observeRepositoryState() {
        val repo = localModelRepository ?: return
        viewModelScope.launch {
            combine(
                repo.availableModels,
                repo.activeModel,
                repo.grantedFolderUris
            ) { models, active, folders ->
                _uiState.value.copy(
                    availableModels = models,
                    activeModel = active,
                    grantedFolders = folders
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }
    }

    /**
     * Triggers a scan of public directories and granted folder trees for local model files.
     */
    fun discoverModels() {
        val repo = localModelRepository ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, errorMessage = null) }
            try {
                repo.discoverLocalModels()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to scan for local models") }
            } finally {
                _uiState.update { it.copy(isScanning = false) }
            }
        }
    }

    /**
     * Adds and persists a user-granted SAF folder tree URI and scans it for models.
     *
     * @param treeUriString SAF directory tree URI string.
     */
    fun addGrantedFolder(treeUriString: String) {
        val repo = localModelRepository ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, errorMessage = null) }
            try {
                repo.addGrantedFolderUri(treeUriString)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add folder tree") }
            } finally {
                _uiState.update { it.copy(isScanning = false) }
            }
        }
    }

    /**
     * Removes a granted folder tree URI.
     *
     * @param treeUriString SAF directory tree URI string to remove.
     */
    fun removeGrantedFolder(treeUriString: String) {
        val repo = localModelRepository ?: return
        viewModelScope.launch {
            repo.removeGrantedFolderUri(treeUriString)
        }
    }

    /**
     * Registers a model selected by the user via Storage Access Framework or file picker.
     *
     * @param uriString Selected Content URI string or file path.
     * @param sourceTag Origin tag for the model file (default: "User Import").
     */
    fun registerModelFromUri(uriString: String, sourceTag: String = "User Import") {
        val repo = localModelRepository ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, errorMessage = null) }
            val result = repo.registerModelUri(uriString, sourceTag)
            result.onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message ?: "Failed to register model file") }
            }
            _uiState.update { it.copy(isScanning = false) }
        }
    }

    /**
     * Selects the specified model as the active model for on-device inference.
     *
     * @param modelId Unique ID of the model to activate.
     */
    fun selectModel(modelId: String) {
        val repo = localModelRepository ?: return
        viewModelScope.launch {
            repo.setActiveModel(modelId)
        }
    }
}
