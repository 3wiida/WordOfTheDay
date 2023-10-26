package com.mahmoudibrahem.wordoftheday

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    val isDarkMode = mutableStateOf(true)
    fun switchMode() {
        isDarkMode.value = !isDarkMode.value
    }
}