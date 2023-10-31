package com.mahmoudibrahem.wordoftheday.data.repository

import android.util.Log
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.core.util.convertToErrorModel
import com.mahmoudibrahem.wordoftheday.data.local.WordDao
import com.mahmoudibrahem.wordoftheday.data.remote.RandomWordAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordAutocompleteAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordsAPI
import com.mahmoudibrahem.wordoftheday.data.remote.dto.SuggestionDto
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordsRepositoryImpl(
    private val wordsAPI: WordsAPI,
    private val randomWordAPI: RandomWordAPI,
    private val wordsSuggestionAPI: WordAutocompleteAPI,
    private val wordDao: WordDao
) : WordsRepository {

    override suspend fun getRandomWord(): String {
        return randomWordAPI.getRandomWord().first()
    }

    override suspend fun getWordDetails(word: String): Flow<Resource<Word>> = flow {
        emit(Resource.Loading())
        val wordDetails = wordDao.getWordDetails(word)?.toWord()
        emit(Resource.Loading(data = wordDetails))

        try {
            val wordFromAPI = wordsAPI.getWordDetails(word).first().toWordEntity()
            wordDao.insertWord(wordFromAPI)
        } catch (e: HttpException) {
            val error = e.convertToErrorModel()
            emit(Resource.Failure(message = error.message, data = wordDetails))
        } catch (e: IOException) {
            emit(
                Resource.Failure(
                    message = "Can't reach server, check your internet",
                    data = wordDetails
                )
            )
        }

        val newWordDetails = wordDao.getWordDetails(word)?.toWord()
        newWordDetails?.let {
            emit(Resource.Success(newWordDetails))
        }
    }

    override suspend fun getWordSuggestions(query: String): List<SuggestionDto> {
        return wordsSuggestionAPI.getSuggestions(query)
    }

    override suspend fun getTodayWord(): Flow<Resource<Word>> = flow {
        emit(Resource.Loading())
        val localTodayWord = wordDao.getTodayWord()?.toWord()
        if (localTodayWord != null) {
            emit(Resource.Success(localTodayWord))
        } else {
            while (true) {
                val randomWord = getRandomWord()
                try {
                    val wordDetails = wordsAPI.getWordDetails(randomWord).first().toWordEntity()
                    wordDao.insertWord(wordDetails.copy(isTodayWord = 1))
                    val newTodayWord = wordDao.getTodayWord()?.toWord()
                    newTodayWord?.let { emit(Resource.Success(it)) }
                    break
                } catch (e: HttpException) {
                    Log.d("```TAG```", "getTodayWord: Not Found")
                } catch (e: IOException) {
                    emit(Resource.Failure(message = "Can't reach server, check your internet"))
                }
            }
        }
    }

    override suspend fun getYesterdayWord(): Flow<Resource<Word>> = flow {
        emit(Resource.Loading())
        val localYesterdayWord = wordDao.getYesterdayWord()
        localYesterdayWord?.let {
            emit(Resource.Success(it.toWord()))
        }
    }

    override suspend fun resetTodayWord() {
        wordDao.resetTodayWord()
    }

    override suspend fun resetYesterdayWord() {
        wordDao.resetYesterdayWord()
        wordDao.getTodayWord()?.let {
            wordDao.insertWord(it.copy(isYesterdayWord = 1))
        }
    }
}