package com.mahmoudibrahem.wordoftheday.domain.usecase

import android.util.Log
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.data.remote.dto.SuggestionDto
import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWordsSuggestionsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(query: String): Flow<Resource<List<Suggestion>>> {
        return wordsRepository.getWordSuggestions(query)
    }

}