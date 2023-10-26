package com.mahmoudibrahem.wordoftheday.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadOnboardingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    readOnboardingStateUseCase: ReadOnboardingStateUseCase,
) : ViewModel() {

    private val _isKeepSplash = MutableStateFlow(true)
    val isKeepSplash = _isKeepSplash.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500)
            _isKeepSplash.update { false }
        }
    }

    val isOnboardingOpened = readOnboardingStateUseCase()

}