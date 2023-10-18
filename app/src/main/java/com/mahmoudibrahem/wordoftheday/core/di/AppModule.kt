package com.mahmoudibrahem.wordoftheday.core.di

import com.mahmoudibrahem.wordoftheday.data.remote.RandomWordAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordAutocompleteAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordsAPI
import com.mahmoudibrahem.wordoftheday.data.repository.WordsRepositoryImpl
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWordsRepository(
        wordsAPI: WordsAPI,
        randomWordAPI: RandomWordAPI,
        wordAutocompleteAPI: WordAutocompleteAPI
    ): WordsRepository {
        return WordsRepositoryImpl(wordsAPI, randomWordAPI, wordAutocompleteAPI)
    }

}