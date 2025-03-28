package com.example.posturepro.features.ble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posturepro.core.data.ble.BleManager
import com.example.posturepro.core.data.ble.DeviceScanner
import com.example.posturepro.core.domain.model.BleConnectionState
import com.example.posturepro.core.domain.model.BleScanResult
import com.example.posturepro.core.domain.model.PostureData
import com.example.posturepro.core.domain.model.PostureStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleViewModel @Inject constructor(
    private val bleManager: BleManager,
    private val deviceScanner: DeviceScanner
) : ViewModel() {

    val connectionState: StateFlow<BleConnectionState> = bleManager.connectionState
    val postureData: StateFlow<PostureData?> = bleManager.postureData
    val batteryLevel: StateFlow<Int?> = bleManager.batteryLevel
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()
    
    val scanResults = deviceScanner.scanResults

    private val _calibrationAngle = MutableStateFlow(0f)
    val calibrationAngle = _calibrationAngle.asStateFlow()

    val postureStatus = combine(postureData, calibrationAngle) { data, calibAngle ->
        data?.let { 
            val adjustedAngle = it.angle - calibAngle
            when {
                adjustedAngle < -DEFAULT_SENSITIVITY -> PostureStatus.Poor
                adjustedAngle > DEFAULT_SENSITIVITY -> PostureStatus.Poor
                adjustedAngle < -DEFAULT_SENSITIVITY/2 -> PostureStatus.Fair
                adjustedAngle > DEFAULT_SENSITIVITY/2 -> PostureStatus.Fair
                else -> PostureStatus.Good
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun startScan() {
        _isScanning.value = true
        deviceScanner.startScan()
    }
    
    fun stopScan() {
        _isScanning.value = false
        deviceScanner.stopScan()
    }
    
    fun connectToDevice(deviceAddress: String) {
        viewModelScope.launch {
            bleManager.connect(deviceAddress)
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            bleManager.disconnect()
        }
    }

    fun setCalibrationAngle(angle: Float) {
        _calibrationAngle.value = angle
    }

    fun getAdjustedAngle(): Float? {
        return postureData.value?.let { it.angle - _calibrationAngle.value }
    }

    companion object {
        private const val DEFAULT_SENSITIVITY = 15 // degrees
    }
}