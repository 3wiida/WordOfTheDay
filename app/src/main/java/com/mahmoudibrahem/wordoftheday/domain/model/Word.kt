package com.mahmoudibrahem.wordoftheday.domain.model

data class Word(
    val word: String,
    val phonetic: String?,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>
) {
    fun checkAudioAvailability(): Boolean {
        return if (phonetics.isNotEmpty()) {
            phonetics.first().audio.isNotEmpty()
        } else {
            false
        }
    }
}
