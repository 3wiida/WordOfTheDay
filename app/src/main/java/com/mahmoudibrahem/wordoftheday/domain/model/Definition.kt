package com.mahmoudibrahem.wordoftheday.domain.model

data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<Any>
) {
    fun getAntonyms(): String {
        var result = ""
        antonyms.forEach { antonym ->
            if (antonym == antonyms.first()) {
                result += " [ "
            }
            result += antonym
            result += if (antonym != antonyms.last()) {
                " - "
            } else {
                " ]"
            }
        }
        return result
    }

    fun getSynonyms(): String {
        var result = ""
        synonyms.forEach { synonym ->
            if (synonym == synonyms.first()) {
                result += " ["
            }
            result += synonym
            result += if (synonym != synonyms.last()) {
                " - "
            } else {
                " ]"
            }
        }
        return result
    }
}
