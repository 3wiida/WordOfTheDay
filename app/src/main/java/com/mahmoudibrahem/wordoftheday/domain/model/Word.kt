package com.mahmoudibrahem.wordoftheday.domain.model

data class Word(
    val word: String,
    val phonetic: String?,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val isTodayWord: Int = 0,
    val isYesterdayWord: Int = 0
) {
    fun checkAudioAvailability(): Boolean {
        return if (phonetics.isNotEmpty()) {
            phonetics.first().audio.isNotEmpty()
        } else {
            false
        }
    }

    fun getWordPartOfSpeech(): String {
        var result = ""
        meanings.forEach {
            result += if (result == "") {
                it.partOfSpeech
            } else {
                " - ${it.partOfSpeech}"
            }
        }
        return result
    }
}
