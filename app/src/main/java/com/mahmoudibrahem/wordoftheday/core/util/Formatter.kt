package com.mahmoudibrahem.wordoftheday.core.util

import com.mahmoudibrahem.wordoftheday.domain.model.Word

object Formatter {

    fun formatWordPartOfSpeech(word: Word): String {
        var result = ""
        word.meanings.forEach {
            result += if (result == "") {
                it.partOfSpeech
            } else {
                " - ${it.partOfSpeech}"
            }
        }
        return result
    }

}