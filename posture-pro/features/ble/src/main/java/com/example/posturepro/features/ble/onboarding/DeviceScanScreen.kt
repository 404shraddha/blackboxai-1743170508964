package com.example.posturepro.features.ble.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.posturepro.R
import com.example.posturepro.core.ui.components.DeviceCard
import com.example.posturepro.core.ui.theme.PostureProTheme
import com.example.posturepro.features.ble.BleViewModel
import no.nordicsemi.android.common.core.scanner.BleScanResult

@Composable
fun DeviceScanScreen(
    onDeviceSelected: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BleViewModel = hiltViewModel()
    val isScanning by viewModel.isScanning.collectAsState()
    val scanResults by viewModel.scanResults.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            
            Text(
                text = stringResource(R.string.select_device),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
            if (isScanning) {
                IconButton(onClick = { viewModel.stopScan() }) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = stringResource(R.string.stop_scan)
                    )
                }
            } else {
                IconButton(onClick = { viewModel.startScan() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.scan)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Device List
        if (scanResults.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Bluetooth,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isScanning) stringResource(R.string.ble_scanning) 
                          else stringResource(R.string.no_devices_found),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(scanResults) { device ->
                    DeviceCard(
                        name = device.name,
                        address = device.address,
                        rssi = device.rssi,
                        onClick = { 
                            viewModel.connectToDevice(device.address)
                            onDeviceSelected() 
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceScanScreenPreview() {
    PostureProTheme {
        DeviceScanScreen(
            onDeviceSelected = {},
            onBack = {}
        )
    }
}