package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion
import com.mahmoudibrahem.wordoftheday.domain.model.Word

data class HomeScreenUIState constructor(
    val searchQuery: String = "",
    val isSearchLoading: Boolean = false,
    val searchResults: List<Suggestion> = emptyList(),
    val screenMsg: String = "",
    val isPageLoading: Boolean = true,
    val isGetRandomWordBtnLoading: Boolean = false,
    val todayWord: Word? = null,
    val yesterdayWord: Word? = null,
    val randomWord: Word? = null
)
