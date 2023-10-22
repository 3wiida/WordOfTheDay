package com.mahmoudibrahem.wordoftheday.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.mahmoudibrahem.wordoftheday.core.navigation.AppNavigation
import com.mahmoudibrahem.wordoftheday.core.navigation.AppScreens
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.WordOfTheDayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.isKeepSplash.value }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )
        )
        setContent {
            WordOfTheDayTheme {
                val startDestination = when (viewModel.startDestination.collectAsState().value) {
                    AppScreens.Home -> AppScreens.Home.route
                    AppScreens.Onboarding -> AppScreens.Onboarding.route
                }
                Log.d("````TAG````", "onCreate: $startDestination")
                val navController = rememberNavController()
                AppNavigation(navController = navController, startDestination = startDestination)
            }
        }
    }
}
