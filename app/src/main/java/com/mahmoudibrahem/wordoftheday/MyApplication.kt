package com.mahmoudibrahem.wordoftheday

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.mahmoudibrahem.wordoftheday.core.AppSettings
import com.mahmoudibrahem.wordoftheday.core.AppSettings.isDarkMode
import com.mahmoudibrahem.wordoftheday.core.AppSettings.isOnboardingOpened
import com.mahmoudibrahem.wordoftheday.core.AppSettings.latestDay
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadDarkModeStateUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadLatestDayUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadOnboardingStateUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var readLatestDayUseCase: ReadLatestDayUseCase

    @Inject
    lateinit var readDarkModeStateUseCase: ReadDarkModeStateUseCase

    @Inject
    lateinit var readOnboardingStateUseCase: ReadOnboardingStateUseCase

    private lateinit var job: Job

    override fun onCreate() {
        super.onCreate()
        job = CoroutineScope(Dispatchers.IO).launch {
            latestDay.intValue = readLatestDayUseCase().first() ?: 0
            isDarkMode.value = readDarkModeStateUseCase().first() ?: false
            isOnboardingOpened.value = readOnboardingStateUseCase().first() ?: false
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        job.cancel()
    }

    override fun onTerminate() {
        job.cancel()
        super.onTerminate()
    }
}