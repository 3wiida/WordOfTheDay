package com.mahmoudibrahem.wordoftheday.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.mahmoudibrahem.wordoftheday.MyApplication
import com.mahmoudibrahem.wordoftheday.core.navigation.AppNavigation
import com.mahmoudibrahem.wordoftheday.core.navigation.AppScreens
import com.mahmoudibrahem.wordoftheday.core.AppSettings.isDarkMode
import com.mahmoudibrahem.wordoftheday.core.AppSettings.latestDay
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.WordOfTheDayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )
        )

        installSplashScreen().setKeepOnScreenCondition { viewModel.isKeepSplash.value }
        lifecycleScope.launch {
            isDarkMode.value = viewModel.isInDarkMode.first() ?: false
            latestDay.intValue = viewModel.latestDay.first() ?: 0
        }
        setContent {
            WordOfTheDayTheme(
                darkTheme = isDarkMode.value
            ) {
                val isOnboardingOpened =
                    viewModel.isOnboardingOpened.collectAsState(initial = false).value ?: false
                AppNavigation(
                    navController = rememberNavController(),
                    startDestination = if (isOnboardingOpened) AppScreens.Home.route else AppScreens.Onboarding.route
                )
            }
        }

    }

}
