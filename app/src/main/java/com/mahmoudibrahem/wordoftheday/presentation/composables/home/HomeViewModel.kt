package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWordsSuggestionsUseCase: GetWordsSuggestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

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

    private fun getWordSuggestions(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _uiState.update { it.copy(searchResults = getWordsSuggestionsUseCase(query)) }
        }
    }

}