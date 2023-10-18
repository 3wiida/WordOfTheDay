package com.mahmoudibrahem.wordoftheday.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RandomWordAPI {
    @GET("word")
    suspend fun getRandomWord(
        @Query("lang") lang: String = "en"
    ): List<String>
}