package com.mahmoudibrahem.wordoftheday.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mahmoudibrahem.wordoftheday.presentation.composables.home.HomeScreen
import com.mahmoudibrahem.wordoftheday.presentation.composables.onboarding.OnBoardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreens.Onboarding.route) {
            OnBoardingScreen(
                onNavigateToHome = {
                    navController.navigate(AppScreens.Home.route)
                }
            )
        }
        composable(route = AppScreens.Home.route) {
            HomeScreen()
        }
    }
}