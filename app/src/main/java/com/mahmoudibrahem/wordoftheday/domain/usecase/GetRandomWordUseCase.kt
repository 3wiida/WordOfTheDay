package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor (
    private val wordsRepository: WordsRepository
) {
    suspend operator fun invoke(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val randomWord = wordsRepository.getRandomWord()
            emit(Resource.Success(randomWord))
        } catch (e: HttpException) {
            e.localizedMessage?.let { emit(Resource.Failure(message = it)) }
        } catch (e: IOException) {
            emit(Resource.Failure(message = "Can't reach server, check your internet"))
        }
    }

}