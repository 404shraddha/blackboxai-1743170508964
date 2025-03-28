package com.example.posturepro.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.posturepro.R
import com.example.posturepro.core.ui.theme.PostureProTheme

@Composable
fun DeviceCard(
    name: String,
    address: String,
    rssi: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Signal strength indicator
                Icon(
                    imageVector = Icons.Default.Bluetooth,
                    contentDescription = stringResource(R.string.signal_strength),
                    tint = when {
                        rssi > -50 -> Color.Green
                        rssi > -70 -> Color(0xFFFFA500) // Orange
                        else -> Color.Red
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$rssi dBm",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun DeviceCardPreview() {
    PostureProTheme {
        DeviceCard(
            name = "PosturePro Wearable",
            address = "AA:BB:CC:DD:EE:FF",
            rssi = -65,
            onClick = {}
        )
    }
}