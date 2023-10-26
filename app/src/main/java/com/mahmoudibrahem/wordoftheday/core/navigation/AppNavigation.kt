package com.mahmoudibrahem.wordoftheday.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mahmoudibrahem.wordoftheday.presentation.composables.home.HomeScreen
import com.mahmoudibrahem.wordoftheday.presentation.composables.onboarding.OnBoardingScreen
import com.mahmoudibrahem.wordoftheday.presentation.composables.single_word.SingleWordScreen

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
            HomeScreen(
                onNavigateToSingleWord = {
                    navController.navigate(
                        AppScreens.SingleWord.route.replace("{word}", it)
                    )
                }
            )
        }
        composable(route = AppScreens.SingleWord.route) {
            val word = it.arguments?.getString("word")
            word?.let {
                SingleWordScreen(
                    word = word,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
        }
    }
}