package com.mahmoudibrahem.wordoftheday.domain.model

import com.mahmoudibrahem.wordoftheday.data.remote.dto.DefinitionDto

data class Meaning (
    val definitions: List<Definition>,
    val partOfSpeech: String
)