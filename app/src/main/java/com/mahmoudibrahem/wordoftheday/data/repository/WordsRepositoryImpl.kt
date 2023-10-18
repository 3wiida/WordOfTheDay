package com.mahmoudibrahem.wordoftheday.data.repository

import com.mahmoudibrahem.wordoftheday.data.remote.RandomWordAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordAutocompleteAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordsAPI
import com.mahmoudibrahem.wordoftheday.data.remote.dto.SuggestionDto
import com.mahmoudibrahem.wordoftheday.data.remote.dto.WordDto
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository

class WordsRepositoryImpl(
    private val wordsAPI: WordsAPI,
    private val randomWordAPI: RandomWordAPI,
    private val wordsSuggestionAPI: WordAutocompleteAPI
) : WordsRepository {

    override suspend fun getRandomWord(): String {
        return randomWordAPI.getRandomWord()[0]
    }

    override suspend fun getWordDetails(word: String): List<WordDto> {
        return wordsAPI.getWordDetails(word)
    }

    override suspend fun getWordSuggestions(query: String): List<SuggestionDto> {
        return wordsSuggestionAPI.getSuggestions(query)
    }

}