package com.example.posturepro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.posturepro.features.ble.onboarding.WelcomeScreen
import com.example.posturepro.features.ble.onboarding.DeviceScanScreen
import com.example.posturepro.features.ble.onboarding.CalibrationScreen
import com.example.posturepro.features.monitoring.HomeScreen

@Composable
fun PostureProNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        // Onboarding Flow
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onContinue = { navController.navigate(Screen.DeviceScan.route) }
            )
        }
        
        composable(Screen.DeviceScan.route) {
            DeviceScanScreen(
                onDeviceSelected = { navController.navigate(Screen.Calibration.route) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Calibration.route) {
            CalibrationScreen(
                onComplete = { navController.navigate(Screen.Home.route) },
                onBack = { navController.popBackStack() }
            )
        }
        
        // Main App Flow
        composable(Screen.Home.route) {
            HomeScreen(
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        
        composable(Screen.Analytics.route) {
            AnalyticsScreen()
        }
        
        composable(Screen.Training.route) {
            TrainingScreen()
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onRecalibrate = { navController.navigate(Screen.Calibration.route) }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object DeviceScan : Screen("device_scan")
    object Calibration : Screen("calibration")
    object Home : Screen("home")
    object Analytics : Screen("analytics")
    object Training : Screen("training")
    object Settings : Screen("settings")
}