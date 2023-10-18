package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke():String {
        return wordsRepository.getRandomWord()
    }
}