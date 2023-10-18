package com.mahmoudibrahem.wordoftheday.domain.repository

import com.mahmoudibrahem.wordoftheday.data.remote.dto.SuggestionDto
import com.mahmoudibrahem.wordoftheday.data.remote.dto.WordDto

interface WordsRepository {

    suspend fun getRandomWord(): String

    suspend fun getWordDetails(word: String): List<WordDto>

    suspend fun getWordSuggestions(query:String) : List<SuggestionDto>

}