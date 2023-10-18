package com.mahmoudibrahem.wordoftheday.domain.model

import com.mahmoudibrahem.wordoftheday.data.remote.dto.MeaningDto
import com.mahmoudibrahem.wordoftheday.data.remote.dto.PhoneticDto

data class Word(
    val word: String,
    val phonetic: String,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>
)
