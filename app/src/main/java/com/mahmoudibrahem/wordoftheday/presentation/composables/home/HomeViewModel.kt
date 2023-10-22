package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetRandomWordUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordDetailsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.StartWordAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWordsSuggestionsUseCase: GetWordsSuggestionsUseCase,
    private val getRandomWordUseCase: GetRandomWordUseCase,
    private val getWordDetailsUseCase: GetWordDetailsUseCase,
    private val startWordAudioUseCase: StartWordAudioUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        getRandomWordForToday()
        getRandomWordForRandomSection()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        if (newQuery.isEmpty()) {
            _uiState.update { it.copy(searchResults = emptyList()) }
        } else {
            getWordSuggestions(query = newQuery)
        }
    }

    fun onSearchFocusChanged(isFocused: Boolean) {
        if (!isFocused) {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    fun onAudioButtonClicked(word: Word) {
        if (word.checkAudioAvailability()) {
            startWordAudioUseCase(word.phonetics.first().audio)
        }
    }

    private fun getRandomWordForToday() {
        viewModelScope.launch(Dispatchers.IO) {
            getRandomWordUseCase().collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isPageLoading = true, screenMsg = "") }
                    }

                    is Resource.Success -> {
                        getTodayWordDetails(state.data.toString())
                    }

                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isPageLoading = false,
                                screenMsg = state.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    fun getRandomWordForRandomSection() {
        viewModelScope.launch(Dispatchers.IO) {
            getRandomWordUseCase().collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isGetRandomWordBtnLoading = true,
                                screenMsg = state.message.toString()
                            )
                        }
                    }

                    is Resource.Success -> {
                        getRandomWordDetails(state.data.toString())
                    }

                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isGetRandomWordBtnLoading = false,
                                screenMsg = state.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getTodayWordDetails(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getWordDetailsUseCase(word).collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (state.data!![0].meanings[0].definitions[0].example.isNullOrEmpty()) {
                            getRandomWordForToday()
                        } else {
                            _uiState.update {
                                it.copy(
                                    todayWord = if (state.data!!.isNotEmpty()) state.data[0] else null,
                                    screenMsg = "",
                                    isPageLoading = false
                                )
                            }
                        }
                    }

                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isPageLoading = false,
                                screenMsg = state.message.toString()
                            )
                        }
                        getRandomWordForToday()
                    }
                }
            }
        }
    }

    private fun getRandomWordDetails(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getWordDetailsUseCase(word).collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (state.data!![0].meanings[0].definitions[0].example.isNullOrEmpty()) {
                            getRandomWordForRandomSection()
                        } else {
                            _uiState.update {
                                it.copy(
                                    isGetRandomWordBtnLoading = false,
                                    randomWord = if (state.data!!.isNotEmpty()) state.data[0] else null,
                                    screenMsg = "",
                                )
                            }
                        }
                    }

                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                screenMsg = state.message.toString()
                            )
                        }
                        getRandomWordForRandomSection()
                    }
                }
            }
        }
    }

    private fun getWordSuggestions(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            getWordsSuggestionsUseCase(query).collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isSearchLoading = true, screenMsg = "") }
                    }

                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isSearchLoading = false,
                                screenMsg = state.message.toString()
                            )
                        }
                    }

                    is Resource.Success -> {
                        state.data?.let { results ->
                            _uiState.update {
                                it.copy(
                                    searchResults = results,
                                    isSearchLoading = false,
                                    screenMsg = ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}