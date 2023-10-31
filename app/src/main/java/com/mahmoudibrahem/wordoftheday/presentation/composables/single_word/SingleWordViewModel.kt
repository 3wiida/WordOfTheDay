package com.mahmoudibrahem.wordoftheday.presentation.composables.single_word

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.core.AppSettings
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.usecase.CopyWordUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordDetailsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.SaveDarkModeStateUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.StartWordAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleWordViewModel @Inject constructor(
    private val getWordDetailsUseCase: GetWordDetailsUseCase,
    private val startWordAudioUseCase: StartWordAudioUseCase,
    private val saveDarkModeStateUseCase: SaveDarkModeStateUseCase,
    private val copyWordUseCase: CopyWordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SingleWordScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun getWordDetails(word: String) {
        viewModelScope.launch {
            getWordDetailsUseCase(word).collectLatest { state ->
                when (state) {
                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isError = state.data == null,
                                errorMsg = state.message.toString(),
                                word = state.data
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, word = state.data) }
                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(word = state.data, isLoading = false) }
                    }
                }
            }
        }
    }

    fun onAudioButtonClicked(word: Word) {
        if (word.checkAudioAvailability()) {
            startWordAudioUseCase(word.phonetics.first().audio)
        }
    }

    fun onCopyClicked(context: Context, word: String) {
        copyWordUseCase(context = context, word = word)
        _uiState.update { it.copy(screenMsg = "Word copied to clipboard") }
    }

    fun onShareClicked(word: Word) {

    }

    fun onChangeModeClicked() {
        AppSettings.switchMode()
        _uiState.update { it.copy(isDarkMode = AppSettings.isDarkMode.value) }
        saveModeInDataStore(AppSettings.isDarkMode.value)
    }

    private fun saveModeInDataStore(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveDarkModeStateUseCase(isDarkMode)
        }
    }

}