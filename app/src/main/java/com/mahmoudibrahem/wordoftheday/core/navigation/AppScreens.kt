package com.mahmoudibrahem.wordoftheday.core.navigation

import com.mahmoudibrahem.wordoftheday.core.navigation.Routes.HOME_SCREEN_ROUTE
import com.mahmoudibrahem.wordoftheday.core.navigation.Routes.ONBOARDING_SCREEN_ROUTE
import com.mahmoudibrahem.wordoftheday.core.navigation.Routes.SINGLE_WORD_ROUTE

sealed class AppScreens(val route: String) {
    object Onboarding : AppScreens(route = ONBOARDING_SCREEN_ROUTE)
    object Home : AppScreens(route = HOME_SCREEN_ROUTE)
    object SingleWord : AppScreens(route = SINGLE_WORD_ROUTE)
}