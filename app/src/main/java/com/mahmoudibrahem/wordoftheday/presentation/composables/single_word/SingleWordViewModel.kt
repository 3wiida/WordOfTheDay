package com.mahmoudibrahem.wordoftheday.presentation.composables.single_word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordDetailsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.StartWordAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleWordViewModel @Inject constructor(
    private val getWordDetailsUseCase: GetWordDetailsUseCase,
    private val startWordAudioUseCase: StartWordAudioUseCase
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
                                isError = true,
                                errorMsg = "There was an error in getting this word from our dictionary"
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(word = state.data?.first(), isLoading = false) }
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

    fun onCopyClicked(word: String) {

    }

    fun onShareClicked(word: Word) {

    }

    fun onChangeModeClicked() {

    }

}