package com.mahmoudibrahem.wordoftheday.data.remote

import com.mahmoudibrahem.wordoftheday.data.remote.dto.WordDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordsAPI {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDetails(
        @Path("word") word: String
    ): List<WordDto>
}