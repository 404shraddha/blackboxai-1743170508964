package com.example.posturepro.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Sensitivity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.posturepro.R
import com.example.posturepro.core.ui.components.PreferenceItem
import com.example.posturepro.features.ble.BleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onRecalibrate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BleViewModel = hiltViewModel()
    var sensitivity by remember { mutableStateOf(15) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Device Section
            Text(
                text = stringResource(R.string.device_settings),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            PreferenceItem(
                icon = Icons.Default.Sensitivity,
                title = stringResource(R.string.sensitivity),
                subtitle = stringResource(R.string.sensitivity_description),
                value = "$sensitivity°",
                onItemClick = { /* Show sensitivity dialog */ }
            )

            PreferenceItem(
                icon = Icons.Default.Replay,
                title = stringResource(R.string.recalibrate),
                subtitle = stringResource(R.string.recalibrate_description),
                onItemClick = onRecalibrate
            )

            // Notifications Section
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            PreferenceItem(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.posture_alerts),
                subtitle = stringResource(R.string.posture_alerts_description),
                trailing = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            )

            // App Section
            Text(
                text = stringResource(R.string.app_settings),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            PreferenceItem(
                title = stringResource(R.string.about),
                subtitle = stringResource(R.string.about_description),
                onItemClick = { /* Show about dialog */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(onBack = {})
    }
}