package com.example.posturepro.features.ble.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun CalibrationScreen(
    onComplete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BleViewModel = hiltViewModel()
    val postureData by viewModel.postureData.collectAsState()
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
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            
            Text(
                text = stringResource(R.string.calibration_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.size(48.dp)) // For balance
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
                angle = postureData?.angle ?: 0f,
                sensitivity = 15,
                modifier = Modifier.size(300.dp)
            )
        }

        // Instructions
        Text(
            text = stringResource(R.string.calibration_instructions),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Action Button
        Button(
            onClick = { 
                viewModel.setCalibrationAngle(postureData?.angle ?: 0f)
                onComplete() 
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = connectionState is BleConnectionState.Connected
        ) {
            Text(text = stringResource(R.string.complete_calibration))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalibrationScreenPreview() {
    PostureProTheme {
        CalibrationScreen(
            onComplete = {},
            onBack = {}
        )
    }
}