package com.mahmoudibrahem.wordoftheday.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion

@Entity(tableName = "search_suggestion_table")
data class SuggestionEntity(
    @PrimaryKey
    val id: Int? = null,
    val suggestion: String
) {
    fun toSuggestion(): Suggestion {
        return Suggestion(word = suggestion)
    }
}
