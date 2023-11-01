package com.mahmoudibrahem.wordoftheday.core

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

object AppSettings {
    val isDarkMode = mutableStateOf(false)
    val latestDay = mutableIntStateOf(0)
    val isOnboardingOpened = mutableStateOf(false)

    fun switchMode() {
        isDarkMode.value = !isDarkMode.value
    }
}