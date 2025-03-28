package com.example.posturepro.core.domain.model

data class BleScanResult(
    val name: String,
    val address: String,
    val rssi: Int
) {
    companion object {
        fun from(scanResult: no.nordicsemi.android.support.v18.scanner.ScanResult): BleScanResult {
            return BleScanResult(
                name = scanResult.device.name ?: "Unknown Device",
                address = scanResult.device.address,
                rssi = scanResult.rssi
            )
        }
    }
}