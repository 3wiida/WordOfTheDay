package com.mahmoudibrahem.wordoftheday.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahmoudibrahem.wordoftheday.domain.model.Meaning
import com.mahmoudibrahem.wordoftheday.domain.model.Phonetic
import com.mahmoudibrahem.wordoftheday.domain.model.Word

@Entity(tableName = "word_table")
data class WordEntity(
    @PrimaryKey val id: Int? = null,
    val word: String,
    val isTodayWord: Int = 0,
    val isYesterdayWord: Int = 0,
    val phonetic: String?,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>
) {
    fun toWord(): Word {
        return Word(
            word = word,
            isTodayWord = isTodayWord,
            isYesterdayWord = isYesterdayWord,
            phonetics = phonetics,
            phonetic = phonetic,
            meanings = meanings
        )
    }
}
