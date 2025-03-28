package com.example.posturepro.core.data.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.posturepro.core.data.ble.model.BleConnectionState
import com.example.posturepro.core.data.ble.model.PostureData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ktx.state.ConnectionState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BleManager @Inject constructor(
    @ApplicationContext private val context: Context
) : BleManager(context) {
    // UUIDs for the PosturePro ESP32 service and characteristics
    companion object {
        const val POSTURE_SERVICE_UUID = "0000FFE0-0000-1000-8000-00805F9B34FB"
        const val POSTURE_DATA_CHARACTERISTIC_UUID = "0000FFE1-0000-1000-8000-00805F9B34FB"
        const val BATTERY_LEVEL_CHARACTERISTIC_UUID = "00002A19-0000-1000-8000-00805F9B34FB"
    }

    private val _connectionState = MutableStateFlow(BleConnectionState.Disconnected)
    val connectionState: StateFlow<BleConnectionState> = _connectionState

    private val _postureData = MutableStateFlow<PostureData?>(null)
    val postureData: StateFlow<PostureData?> = _postureData

    private val _batteryLevel = MutableStateFlow<Int?>(null)
    val batteryLevel: StateFlow<Int?> = _batteryLevel

    private var postureCharacteristic: BluetoothGattCharacteristic? = null
    private var batteryCharacteristic: BluetoothGattCharacteristic? = null

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun connect(device: BluetoothDevice): Boolean {
        _connectionState.value = BleConnectionState.Connecting
        return super.connect(device)
            .retry(3, 100)
            .useAutoConnect(false)
            .enqueue()
    }

    override fun getGattCallback(): BluetoothGattCallback {
        return object : BleManagerGattCallback() {
            override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
                val service = gatt.getService(POSTURE_SERVICE_UUID.toUUID())
                postureCharacteristic = service?.getCharacteristic(POSTURE_DATA_CHARACTERISTIC_UUID.toUUID())
                batteryCharacteristic = service?.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC_UUID.toUUID())
                
                return postureCharacteristic != null && batteryCharacteristic != null
            }

            override fun onDeviceDisconnected() {
                _connectionState.value = BleConnectionState.Disconnected
                _postureData.value = null
                _batteryLevel.value = null
            }

            override fun onDeviceReady() {
                _connectionState.value = BleConnectionState.Connected
                setNotificationCallback(postureCharacteristic)
                    .with { device, data ->
                        val angle = data.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 0)
                        _postureData.value = PostureData(angle.toFloat())
                    }
                enableNotifications(postureCharacteristic).enqueue()

                readCharacteristic(batteryCharacteristic)
                    .with { device, data ->
                        _batteryLevel.value = data.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)
                    }
                    .enqueue()
            }
        }
    }

    fun String.toUUID() = java.util.UUID.fromString(this)
}