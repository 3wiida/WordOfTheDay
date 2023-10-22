package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWordDetailsUseCase (
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(word: String): Flow<Resource<List<Word>>> = flow {
        try {
            val wordDetails = wordsRepository.getWordDetails(word).map { it.toWord() }
            emit(Resource.Success(wordDetails))
        } catch (e: HttpException) {
            e.localizedMessage?.let { emit(Resource.Failure(message = it)) }
        } catch (e: IOException) {
            emit(Resource.Failure(message = "Can't reach server, check your internet"))
        }
    }
}