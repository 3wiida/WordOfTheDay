package com.mahmoudibrahem.wordoftheday.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.mahmoudibrahem.wordoftheday.core.navigation.AppNavigation
import com.mahmoudibrahem.wordoftheday.core.navigation.AppScreens
import com.mahmoudibrahem.wordoftheday.core.AppSettings.isDarkMode
import com.mahmoudibrahem.wordoftheday.core.AppSettings.isOnboardingOpened
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.WordOfTheDayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keepSplashCondition = MutableStateFlow(true)

        actionBar?.hide()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            )
        )

        val splashScreen = installSplashScreen()

        lifecycleScope.launch {
            delay(1500)
            keepSplashCondition.update { false }
        }

        splashScreen.setKeepOnScreenCondition {
            keepSplashCondition.value
        }

        setContent {
            WordOfTheDayTheme(
                darkTheme = isDarkMode.value
            ) {
                AppNavigation(
                    navController = rememberNavController(),
                    startDestination = if (isOnboardingOpened.value) AppScreens.Home.route else AppScreens.Onboarding.route
                )
            }
        }

    }

}
