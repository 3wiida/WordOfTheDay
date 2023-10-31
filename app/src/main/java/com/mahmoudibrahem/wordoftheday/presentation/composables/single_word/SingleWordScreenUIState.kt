package com.mahmoudibrahem.wordoftheday.presentation.composables.single_word

import com.mahmoudibrahem.wordoftheday.core.AppSettings
import com.mahmoudibrahem.wordoftheday.domain.model.Word

data class SingleWordScreenUIState(
    val word: Word? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMsg: String = "",
    val isDarkMode: Boolean = AppSettings.isDarkMode.value,
    val screenMsg: String = ""
)