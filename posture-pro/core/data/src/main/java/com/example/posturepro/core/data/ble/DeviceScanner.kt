package com.example.posturepro.core.data.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.posturepro.core.domain.model.BleScanResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scanner = BluetoothLeScannerCompat.getScanner()
    private val _scanResults = MutableStateFlow<List<BleScanResult>>(emptyList())
    val scanResults: Flow<List<BleScanResult>> = _scanResults

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val scanResult = BleScanResult(
                name = device.name ?: "Unknown Device",
                address = device.address,
                rssi = result.rssi
            )
            
            _scanResults.value = _scanResults.value
                .filterNot { it.address == device.address }
                .plus(scanResult)
                .sortedByDescending { it.rssi }
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        _scanResults.value = emptyList()
        
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(0)
            .build()

        val filters = listOf(
            ScanFilter.Builder()
                .setServiceUuid(BleManager.POSTURE_SERVICE_UUID.toUUID())
                .build()
        )

        scanner.startScan(filters, settings, scanCallback)
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        scanner.stopScan(scanCallback)
    }

    private fun String.toUUID() = java.util.UUID.fromString(this)
}