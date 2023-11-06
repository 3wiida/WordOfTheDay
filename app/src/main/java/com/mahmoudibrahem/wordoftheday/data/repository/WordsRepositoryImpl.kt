package com.mahmoudibrahem.wordoftheday.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.mahmoudibrahem.wordoftheday.core.util.Resource
import com.mahmoudibrahem.wordoftheday.core.util.convertToErrorModel
import com.mahmoudibrahem.wordoftheday.data.local.WordDatabase
import com.mahmoudibrahem.wordoftheday.data.remote.RandomWordAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordAutocompleteAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordsAPI
import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion
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
    private val db: WordDatabase
) : WordsRepository {

    override suspend fun getRandomWord(): String {
        return randomWordAPI.getRandomWord().first()
    }

    override suspend fun getWordDetails(word: String): Flow<Resource<Word>> = flow {
        emit(Resource.Loading())
        val localWord = db.wordDao.getWordDetails(word)?.toWord()
        try {
            val wordFromAPI = wordsAPI.getWordDetails(word).first().toWordEntity()
            db.withTransaction {
                db.wordDao.deleteWord(word)
                db.wordDao.insertWord(wordFromAPI)
            }
        } catch (e: HttpException) {
            val error = e.convertToErrorModel()
            emit(
                Resource.Failure(
                    message = error.message,
                    data = localWord
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Failure(
                    message = "Can't reach server, check your internet",
                    data = localWord
                )
            )
        } catch (e: Exception) {
            Log.d("```TAG```", "invoke: ${e.printStackTrace()}")
        }
        val newWordDetails = db.wordDao.getWordDetails(word)?.toWord()
        newWordDetails?.let {
            emit(Resource.Success(newWordDetails))
        }
    }

    override suspend fun getWordSuggestions(query: String): Flow<Resource<List<Suggestion>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val suggestions =
                    wordsSuggestionAPI.getSuggestions(query).map { it.toSuggestionEntity() }
                db.withTransaction {
                    db.wordDao.deleteOldSuggestions(query)
                    suggestions.forEach { db.wordDao.insertSuggestions(it) }
                }
            } catch (e: HttpException) {
                val error = e.convertToErrorModel()
                emit(
                    Resource.Failure(
                        message = error.message
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Failure(
                        message = "Can't reach server, check your internet"
                    )
                )
            } catch (e: Exception) {
                Log.d("```TAG```", "invoke: ${e.printStackTrace()}")
            }
            val newSuggestions = db.wordDao.getSuggestions(query)
            emit(Resource.Success(data = newSuggestions.map { it.toSuggestion() }))
        }
    }

    override suspend fun getTodayWord(): Flow<Resource<Word>> = flow {
        emit(Resource.Loading())
        val localTodayWord = db.wordDao.getTodayWord()?.toWord()
        if (localTodayWord != null) {
            emit(Resource.Success(localTodayWord))
        } else {
            while (true) {
                val randomWord = getRandomWord()
                try {
                    val wordDetails = wordsAPI.getWordDetails(randomWord).first().toWordEntity()
                    db.wordDao.insertWord(wordDetails.copy(isTodayWord = 1))
                    val newTodayWord = db.wordDao.getTodayWord()?.toWord()
                    newTodayWord?.let { emit(Resource.Success(it)) }
                    break
                } catch (e: HttpException) {
                    Log.d("```TAG```", "getTodayWord: Not Found")
                } catch (e: IOException) {
                    emit(Resource.Failure(message = "Can't reach server, check your internet"))
                } catch (e: Exception) {
                    Log.d("```TAG```", "invoke: ${e.printStackTrace()}")
                }
            }
        }
    }

    override suspend fun getYesterdayWord(): Flow<Resource<Word>> = flow {
        emit(Resource.Loading())
        val localYesterdayWord = db.wordDao.getYesterdayWord()
        localYesterdayWord?.let { emit(Resource.Success(it.toWord())) }
    }

    override suspend fun resetHomeTodaySection() {
        db.withTransaction {
            db.wordDao.resetYesterdayWord()
            db.wordDao.getTodayWord()?.let {
                db.wordDao.insertWord(it.copy(isYesterdayWord = 1))
            }
            db.wordDao.resetTodayWord()
        }
    }
}