package com.example.androidmaiden.data.hardware.repository

import com.example.androidmaiden.domain.hardware.model.*
import com.example.androidmaiden.domain.hardware.repository.HardwareRepository
import com.example.androidmaiden.platform.getPlatformSystemResources
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

/**
 * Repository implementation for managing hardware telemetry.
 * Provides periodic updates and simulates CPU Stress Testing with high loads.
 */
class HardwareRepositoryImpl : HardwareRepository {

    // Stress testing state flows
    private val _isStressTesting = MutableStateFlow(false)
    private var stressTestJob: Job? = null
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Current temperature simulation state during stress test
    private var simulatedTemperature = 34.0f

    override fun observeSystemResources(pollIntervalMs: Long): Flow<SystemResources> = flow {
        while (currentCoroutineContext().isActive) {
            val baseResources = getPlatformSystemResources()
            val isStressActive = _isStressTesting.value
            
            // Adjust metrics if CPU Stress Test is running
            val adjustedResources = if (isStressActive) {
                // Simulate temperature build-up
                if (simulatedTemperature < 47.5f) {
                    simulatedTemperature += 0.4f
                }

                // Make all CPU cores work at 95% - 100% capacity
                val spikedCores = baseResources.cpuCores.map { core ->
                    core.copy(
                        usagePercentage = 95f + Random.nextFloat() * 5f,
                        frequencyMhz = core.frequencyMhz + 800 // Boost frequency
                    )
                }

                // RAM usage increases during computation
                val increasedUsedBytes = (baseResources.ramState.usedBytes + (512L * 1024L * 1024L)).coerceAtMost(baseResources.ramState.totalBytes)
                val spikedRam = baseResources.ramState.copy(
                    usedBytes = increasedUsedBytes,
                    freeBytes = baseResources.ramState.totalBytes - increasedUsedBytes
                )

                // Battery temperature increases, voltage drops under high load
                val spikedBattery = baseResources.batteryState.copy(
                    temperatureCelsius = simulatedTemperature,
                    voltageMv = (baseResources.batteryState.voltageMv - 150).coerceAtLeast(3500)
                )

                baseResources.copy(
                    cpuCores = spikedCores,
                    ramState = spikedRam,
                    batteryState = spikedBattery,
                    timestamp = System.currentTimeMillis()
                )
            } else {
                // Cool down temperature gradually
                if (simulatedTemperature > baseResources.batteryState.temperatureCelsius) {
                    simulatedTemperature -= 0.2f
                } else {
                    simulatedTemperature = baseResources.batteryState.temperatureCelsius
                }
                
                baseResources.copy(
                    batteryState = baseResources.batteryState.copy(
                        temperatureCelsius = simulatedTemperature
                    )
                )
            }

            emit(adjustedResources)
            delay(pollIntervalMs)
        }
    }.flowOn(Dispatchers.Default)

    override fun triggerStressTest(durationMs: Long) {
        stressTestJob?.cancel()
        _isStressTesting.value = true
        
        stressTestJob = repositoryScope.launch {
            delay(durationMs)
            _isStressTesting.value = false
        }
    }

    override fun isStressTesting(): Flow<Boolean> = _isStressTesting.asStateFlow()

    override fun stopStressTest() {
        stressTestJob?.cancel()
        _isStressTesting.value = false
    }
}
