package com.mahmoudibrahem.wordoftheday.data.remote.dto

import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion

data class SuggestionDto(
    val score: Int,
    val word: String
) {
    fun toSuggestion(): Suggestion {
        return Suggestion(word = word)
    }
}