package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.core.AppSettings
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetRandomWordUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetTodayWordUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordDetailsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetYesterdayWordUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.SaveDarkModeStateUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.StartWordAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    private val saveDarkModeStateUseCase: SaveDarkModeStateUseCase,
    private val getTodayWordUseCase: GetTodayWordUseCase,
    private val getYesterdayWordUseCase: GetYesterdayWordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        getTodayWord()
        getYesterdayWord()
        getRandomWordForRandomSection()
    }

    fun onchangeModeClicked() {
        AppSettings.switchMode()
        _uiState.update { it.copy(isDarkMode = AppSettings.isDarkMode.value) }
        saveModeInDataStore(AppSettings.isDarkMode.value)
    }

    private fun saveModeInDataStore(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveDarkModeStateUseCase(isDarkMode)
        }
    }


    fun onSearchQueryChanged(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        if (newQuery.isEmpty()) {
            searchJob?.cancel()
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

    fun onScreenResumed() {
        _uiState.update { it.copy(isDarkMode = AppSettings.isDarkMode.value) }
    }


    fun getRandomWordForRandomSection() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
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

    private fun getTodayWord() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
            getTodayWordUseCase().collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        _uiState.update { it.copy(todayWord = state.data) }
                    }

                    is Resource.Failure -> {

                    }
                }
            }
        }
    }

    private fun getYesterdayWord() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
            getYesterdayWordUseCase().collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        Log.d("```TAG```", "getTodayWord: ${state.data}")
                        _uiState.update { it.copy(yesterdayWord = state.data) }
                    }

                    is Resource.Failure -> {}
                }
            }
        }
    }

    private fun getRandomWordDetails(word: String) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
            getWordDetailsUseCase(word).collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (state.data!!.meanings[0].definitions[0].example.isNullOrEmpty()) {
                            getRandomWordForRandomSection()
                        } else {
                            _uiState.update {
                                it.copy(
                                    isGetRandomWordBtnLoading = false,
                                    randomWord = state.data,
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
        searchJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
            delay(500)
            getWordsSuggestionsUseCase(query).collectLatest { state ->
                when (state) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isSearchLoading = true,
                                screenMsg = "",
                                searchResults = state.data ?: emptyList()
                            )
                        }
                    }

                    is Resource.Failure -> {
                        _uiState.update {
                            it.copy(
                                isSearchLoading = false,
                                screenMsg = state.message.toString(),
                                searchResults = state.data ?: emptyList()
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