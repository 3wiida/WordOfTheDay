package com.mahmoudibrahem.wordoftheday.core.di

import com.mahmoudibrahem.wordoftheday.core.util.Constants.RANDOM_WORD_API_BASE_URL
import com.mahmoudibrahem.wordoftheday.core.util.Constants.WORDS_API_BASE_URL
import com.mahmoudibrahem.wordoftheday.core.util.Constants.WORD_SUGGESTIONS_BASE_URL
import com.mahmoudibrahem.wordoftheday.data.remote.RandomWordAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordAutocompleteAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideWordsAPI(): WordsAPI {
        return Retrofit.Builder()
            .baseUrl(WORDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRandomWordAPI(): RandomWordAPI {
        return Retrofit.Builder()
            .baseUrl(RANDOM_WORD_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomWordAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideWordAutoCompleteAPI():WordAutocompleteAPI{
        return Retrofit.Builder()
            .baseUrl(WORD_SUGGESTIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordAutocompleteAPI::class.java)
    }

}