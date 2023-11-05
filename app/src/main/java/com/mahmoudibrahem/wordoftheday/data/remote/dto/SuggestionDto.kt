package com.mahmoudibrahem.wordoftheday.data.remote.dto

import com.mahmoudibrahem.wordoftheday.data.local.entity.SuggestionEntity

data class SuggestionDto(
    val score: Int,
    val word: String
) {
    fun toSuggestionEntity(): SuggestionEntity {
        return SuggestionEntity(suggestion = word)
    }
}