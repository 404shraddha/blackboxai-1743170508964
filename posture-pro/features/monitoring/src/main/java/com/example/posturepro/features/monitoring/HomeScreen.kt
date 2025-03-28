package com.example.posturepro.features.monitoring

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.posturepro.R
import com.example.posturepro.core.ui.components.PostureIndicator
import com.example.posturepro.core.ui.theme.PostureProTheme
import com.example.posturepro.features.ble.BleViewModel

@Composable
fun HomeScreen(
    onSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: BleViewModel = hiltViewModel()
    val postureStatus by viewModel.postureStatus.collectAsState()
    val adjustedAngle by derivedStateOf { viewModel.getAdjustedAngle() }
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

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
            Text(
                text = stringResource(R.string.posture_status),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
            IconButton(onClick = onSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Posture Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            PostureIndicator(
                angle = adjustedAngle ?: 0f,
                sensitivity = 15,
                modifier = Modifier.size(300.dp)
            )
        }

        // Status and Battery
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    when (postureStatus) {
                        PostureStatus.Good -> R.string.posture_good
                        PostureStatus.Fair -> R.string.posture_fair
                        PostureStatus.Poor -> R.string.posture_poor
                        null -> R.string.posture_unknown
                    }
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = when (postureStatus) {
                    PostureStatus.Good -> MaterialTheme.colorScheme.primary
                    PostureStatus.Fair -> MaterialTheme.colorScheme.secondary
                    PostureStatus.Poor -> MaterialTheme.colorScheme.error
                    null -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }
            )

            if (batteryLevel != null) {
                Text(
                    text = "$batteryLevel%",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PostureProTheme {
        HomeScreen(onSettings = {})
    }
}