package com.example.androidmaiden.presentation.ui.screens.hardware

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.adaptive.LocalWindowSizeClass
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.viewmodel.HardwareUiState
import com.example.androidmaiden.platform.stringResource
import com.example.androidmaiden.presentation.ui.screens.hardware.components.BatteryCard
import com.example.androidmaiden.presentation.ui.screens.hardware.components.CpuCoreGrid
import com.example.androidmaiden.presentation.ui.screens.hardware.components.CpuWaveform
import com.example.androidmaiden.presentation.ui.screens.hardware.components.RamGauge
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The Stateless Content for the Hardware Monitor screen.
 *
 * This component is responsible for laying out the hardware telemetry components in an
 * adaptive manner, responding to changes in the window size.
 *
 * It features:
 * - A real-time CPU usage waveform graph.
 * - Controls for triggering and stopping a CPU stress test.
 * - A gauge for monitoring RAM utilization.
 * - A card for battery status and health.
 * - A grid displaying detailed per-core logical processor information.
 *
 * @param uiState The current state of the hardware monitor, including telemetry and stress test status.
 * @param onBack Callback invoked when the user requests to navigate back.
 * @param onStartStress Callback invoked to trigger a stress test with a specified duration in ms.
 * @param onStopStress Callback invoked to stop any currently running stress test.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardwareContent(
    uiState: HardwareUiState,
    onBack: () -> Unit,
    onStartStress: (Long) -> Unit,
    onStopStress: () -> Unit
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val isWideScreen = windowSizeClass.widthCategory != WindowSizeCategory.Compact
    var showTestMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = "hardware_monitor_title")) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.isStressTesting) {
                        val remainingSec = uiState.stressTestTimeRemainingMs / 1000f
                        val timeStr = ((remainingSec * 10).toInt() / 10.0).toString()
                        
                        Text(
                            text = "${timeStr}s",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = onStopStress) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop Stress Test",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        Box {
                            IconButton(onClick = { showTestMenu = true }) {
                                Icon(Icons.Default.Bolt, contentDescription = "Test Features")
                            }
                            DropdownMenu(
                                expanded = showTestMenu,
                                onDismissRequest = { showTestMenu = false }
                            ) {
                                Text(
                                    text = "Start Stress Test",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = "hardware_duration_10s")) },
                                    onClick = {
                                        onStartStress(10000L)
                                        showTestMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = "hardware_duration_30s")) },
                                    onClick = {
                                        onStartStress(30000L)
                                        showTestMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = "hardware_duration_60s")) },
                                    onClick = {
                                        onStartStress(60000L)
                                        showTestMenu = false
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isWideScreen) {
                // Wide Screen Layout (Tablets/Desktop)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.5f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CpuWaveform(
                            history = uiState.cpuUsageHistory,
                            modifier = Modifier.fillMaxWidth()
                        )

                        CpuCoreGrid(
                            cores = uiState.telemetry?.cpuCores ?: emptyList(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RamGauge(
                            ramState = uiState.telemetry?.ramState,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                        )

                        BatteryCard(
                            batteryState = uiState.telemetry?.batteryState,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                // Compact Screen Layout (Phones)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CpuWaveform(
                        history = uiState.cpuUsageHistory,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        RamGauge(
//                            ramState = uiState.telemetry?.ramState,
//                            modifier = Modifier.weight(1f)
//                        )
                        BatteryCard(
                            batteryState = uiState.telemetry?.batteryState,
                            modifier = Modifier.weight(1.5f)
                        )
                    }

                    CpuCoreGrid(
                        cores = uiState.telemetry?.cpuCores ?: emptyList(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HardwareContentPreview() {
    AppTheme {
        HardwareContent(
            uiState = HardwareUiState(),
            onBack = {},
            onStartStress = {},
            onStopStress = {}
        )
    }
}
