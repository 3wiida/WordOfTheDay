package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWordDetailsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(word: String): Flow<Resource<Word>> {
        return wordsRepository.getWordDetails(word)
    }

}