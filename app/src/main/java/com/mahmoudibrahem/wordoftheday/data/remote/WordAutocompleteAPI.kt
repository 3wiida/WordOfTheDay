package com.mahmoudibrahem.wordoftheday.data.remote

import com.mahmoudibrahem.wordoftheday.data.remote.dto.SuggestionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WordAutocompleteAPI {
     @GET("sug")
     suspend fun getSuggestions(
         @Query("s") query:String
     ):List<SuggestionDto>
}