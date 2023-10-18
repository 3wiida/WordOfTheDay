package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import javax.inject.Inject

class GetWordsSuggestionsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(query: String): List<Suggestion> {
        return wordsRepository.getWordSuggestions(query).map { it.toSuggestion() }
    }
}