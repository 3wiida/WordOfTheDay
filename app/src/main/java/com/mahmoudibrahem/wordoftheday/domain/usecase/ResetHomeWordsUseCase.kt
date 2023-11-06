package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import javax.inject.Inject

class ResetHomeWordsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke() {
        wordsRepository.resetHomeTodaySection()
    }
}