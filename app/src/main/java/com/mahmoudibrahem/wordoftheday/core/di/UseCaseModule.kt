package com.mahmoudibrahem.wordoftheday.core.di

import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideWordSuggestionsUseCase(wordsRepository: WordsRepository): GetWordsSuggestionsUseCase {
        return GetWordsSuggestionsUseCase(wordsRepository)
    }

}