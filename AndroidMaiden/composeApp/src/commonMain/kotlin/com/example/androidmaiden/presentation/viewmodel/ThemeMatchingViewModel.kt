package com.example.androidmaiden.presentation.viewmodel

import com.example.androidmaiden.domain.themematching.model.*
import com.example.androidmaiden.domain.themematching.repository.ThemeMatchingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Data class representing the UI state of the Theme Matching screen.
 */
data class ThemeMatchingUiState(
    val activeAction: CharacterAction = CharacterAction.IDLE,
    val speedScale: Float = 1.0f,
    val isPlaying: Boolean = true,
    val selectedTableTab: Int = 0,
    val showGuide: Boolean = true,
    val actions: List<CharacterAction> = emptyList(),
    val stats: List<CharacterStats> = emptyList(),
    val configs: List<AnimationConfig> = emptyList()
)

/**
 * ViewModel responsible for managing and driving the UI state of the Theme Matching feature.
 */
class ThemeMatchingViewModel(
    private val repository: ThemeMatchingRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ThemeMatchingUiState())
    val uiState: StateFlow<ThemeMatchingUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    /**
     * Loads the initial domain lists from the repository.
     */
    private fun loadInitialData() {
        val actions = repository.getCharacterActions()
        val stats = repository.getCharacterStats()
        val configs = repository.getAnimationConfigs()
        _uiState.update {
            it.copy(
                actions = actions,
                stats = stats,
                configs = configs
            )
        }
    }

    /**
     * Selects active character action.
     */
    fun selectAction(action: CharacterAction) {
        _uiState.update { it.copy(activeAction = action) }
    }

    /**
     * Updates the speed scale multiplier.
     */
    fun updateSpeedScale(speed: Float) {
        _uiState.update { it.copy(speedScale = speed) }
    }

    /**
     * Toggles whether the animation playback is playing or paused.
     */
    fun togglePlayback() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    /**
     * Selects active table tab.
     */
    fun selectTableTab(tab: Int) {
        _uiState.update { it.copy(selectedTableTab = tab) }
    }

    /**
     * Toggles whether the animation concept guide card is expanded.
     */
    fun toggleGuide() {
        _uiState.update { it.copy(showGuide = !it.showGuide) }
    }
}
