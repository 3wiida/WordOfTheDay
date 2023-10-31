package com.mahmoudibrahem.wordoftheday.data.remote.dto

import com.mahmoudibrahem.wordoftheday.data.local.entity.WordEntity
import com.mahmoudibrahem.wordoftheday.domain.model.Word

data class WordDto(
    val license: LicenseDto,
    val meanings: List<MeaningDto>,
    val phonetic: String?,
    val phonetics: List<PhoneticDto>,
    val sourceUrls: List<String>,
    val word: String
) {
    fun toWordEntity(): WordEntity {
        return WordEntity(
            word = word,
            phonetic = phonetic,
            meanings = meanings.map { it.toMeaning() },
            phonetics = phonetics.map { it.toPhonetic() }
        )
    }
}