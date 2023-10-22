package com.mahmoudibrahem.wordoftheday.data.remote.dto

import com.mahmoudibrahem.wordoftheday.domain.model.Definition

data class DefinitionDto(
    val antonyms: List<Any>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
) {
    fun toDefinition(): Definition {
        return Definition(
            definition = definition,
            example = example
        )
    }
}