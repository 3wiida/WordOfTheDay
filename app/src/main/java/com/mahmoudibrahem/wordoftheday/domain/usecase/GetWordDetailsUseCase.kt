package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import javax.inject.Inject

class GetWordDetailsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(word: String): List<Word> {
        return wordsRepository.getWordDetails(word).map { it.toWord() }
    }
}