package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion

data class HomeScreenUIState(
    val searchQuery: String = "",
    val searchResults: List<Suggestion> = emptyList()
)
