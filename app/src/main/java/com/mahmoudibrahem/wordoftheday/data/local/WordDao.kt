package com.mahmoudibrahem.wordoftheday.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahmoudibrahem.wordoftheday.data.local.entity.SuggestionEntity
import com.mahmoudibrahem.wordoftheday.data.local.entity.WordEntity

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table WHERE word=:word")
    suspend fun getWordDetails(word: String): WordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Query("DELETE FROM WORD_TABLE WHERE word=:word")
    suspend fun deleteWord(word: String)

    @Query("SELECT * FROM word_table Where isTodayWord = 1")
    suspend fun getTodayWord(): WordEntity?

    @Query("SELECT * FROM word_table WHERE isYesterdayWord = 1")
    suspend fun getYesterdayWord(): WordEntity?

    @Query("UPDATE word_table SET isTodayWord = 0 WHERE isTodayWord=1")
    suspend fun resetTodayWord()

    @Query("UPDATE word_table SET isYesterdayWord = 0 WHERE isYesterdayWord=1")
    suspend fun resetYesterdayWord()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestions(suggestion: SuggestionEntity)

    @Query("SELECT * FROM search_suggestion_table WHERE suggestion LIKE '%' || :query || '%' ")
    suspend fun getSuggestions(query: String): List<SuggestionEntity>

    @Query("DELETE FROM search_suggestion_table WHERE suggestion LIKE  '%' || :query || '%' ")
    suspend fun deleteOldSuggestions(query: String)
}