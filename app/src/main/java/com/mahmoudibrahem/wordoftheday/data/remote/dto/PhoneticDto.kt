package com.mahmoudibrahem.wordoftheday.data.remote.dto

import com.mahmoudibrahem.wordoftheday.domain.model.Phonetic

data class PhoneticDto(
    val audio: String,
    val license: LicenseDto,
    val sourceUrl: String,
    val text: String
) {
    fun toPhonetic(): Phonetic {
        return Phonetic(
            audio = audio
        )
    }
}