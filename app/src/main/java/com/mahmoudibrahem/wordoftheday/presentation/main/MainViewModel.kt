package com.mahmoudibrahem.wordoftheday.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.core.navigation.AppScreens
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadOnboardingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readOnboardingStateUseCase: ReadOnboardingStateUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<AppScreens>(AppScreens.Onboarding)
    val startDestination = _startDestination.asStateFlow()

    private val _isKeepSplash = MutableStateFlow(true)
    val isKeepSplash = _isKeepSplash.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            readOnboardingOpenState()
        }
    }

    private fun readOnboardingOpenState() {
        viewModelScope.launch(Dispatchers.IO) {
            readOnboardingStateUseCase().collectLatest { state ->
                if (state == null) {
                    _startDestination.update { AppScreens.Home }
                } else {
                    if (state) {
                        _startDestination.update { AppScreens.Home }
                    } else {
                        _startDestination.update { AppScreens.Onboarding }
                    }
                }
                _isKeepSplash.update { false }
            }
        }
    }

}