package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.domain.hardware.model.*
import com.example.androidmaiden.domain.hardware.repository.HardwareRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * UI State for the Hardware Monitor screen.
 */
data class HardwareUiState(
    val telemetry: SystemResources? = null,
    val cpuUsageHistory: List<Float> = emptyList(),
    val isStressTesting: Boolean = false,
    val stressTestTimeRemainingMs: Long = 0L
)

/**
 * ViewModel responsible for driving state and handles operations of the Hardware Monitor screen.
 */
class HardwareViewModel(
    private val repository: HardwareRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HardwareUiState())
    val uiState: StateFlow<HardwareUiState> = _uiState.asStateFlow()

    private var telemetryJob: Job? = null
    private var stressTimerJob: Job? = null

    init {
        startTelemetryPolling()
        observeStressTestingState()
    }

    /**
     * Start observing system resources flow.
     */
    private fun startTelemetryPolling() {
        telemetryJob?.cancel()
        telemetryJob = repository.observeSystemResources(pollIntervalMs = 1000L)
            .onEach { resources ->
                _uiState.update { state ->
                    // Calculate average CPU load across all cores
                    val avgCpuUsage = if (resources.cpuCores.isNotEmpty()) {
                        resources.cpuCores.map { it.usagePercentage }.average().toFloat()
                    } else {
                        0f
                    }

                    // Keep a sliding window of the last 30 CPU usage points
                    val updatedHistory = (state.cpuUsageHistory + avgCpuUsage).takeLast(30)
                    
                    state.copy(
                        telemetry = resources,
                        cpuUsageHistory = updatedHistory
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Start observing repository stress test state changes.
     */
    private fun observeStressTestingState() {
        repository.isStressTesting()
            .onEach { isTesting ->
                _uiState.update { it.copy(isStressTesting = isTesting) }
                if (!isTesting) {
                    stressTimerJob?.cancel()
                    _uiState.update { it.copy(stressTestTimeRemainingMs = 0L) }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Triggers a CPU stress test for the specified duration.
     * @param durationMs duration of the stress test in milliseconds.
     */
    fun startStressTest(durationMs: Long) {
        repository.triggerStressTest(durationMs)
        
        // Start countdown timer for UI feedback
        stressTimerJob?.cancel()
        stressTimerJob = viewModelScope.launch {
            var remaining = durationMs
            while (remaining > 0) {
                _uiState.update { it.copy(stressTestTimeRemainingMs = remaining) }
                delay(100L)
                remaining -= 100L
            }
            _uiState.update { it.copy(stressTestTimeRemainingMs = 0L) }
        }
    }

    /**
     * Stops the CPU stress test immediately.
     */
    fun stopStressTest() {
        repository.stopStressTest()
        stressTimerJob?.cancel()
        _uiState.update { it.copy(stressTestTimeRemainingMs = 0L) }
    }

    override fun onCleared() {
        super.onCleared()
        telemetryJob?.cancel()
        stressTimerJob?.cancel()
    }
}
