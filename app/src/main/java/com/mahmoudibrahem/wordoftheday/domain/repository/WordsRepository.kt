package com.mahmoudibrahem.wordoftheday.domain.repository

import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.data.remote.dto.SuggestionDto
import com.mahmoudibrahem.wordoftheday.data.remote.dto.WordDto
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordsRepository {

    suspend fun getRandomWord(): String

    suspend fun getWordDetails(word: String): Flow<Resource<Word>>

    suspend fun getWordSuggestions(query: String): List<SuggestionDto>

    suspend fun getTodayWord(): Flow<Resource<Word>>

    suspend fun getYesterdayWord(): Flow<Resource<Word>>

    suspend fun resetTodayWord()

    suspend fun resetYesterdayWord()

}