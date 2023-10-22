package com.mahmoudibrahem.wordoftheday.core.di

import android.media.MediaPlayer
import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetRandomWordUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordDetailsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadOnboardingStateUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.SaveOnboardingStateUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.StartWordAudioUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
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

    @Provides
    @Singleton
    fun provideGetRandomWordUseCase(wordsRepository: WordsRepository): GetRandomWordUseCase {
        return GetRandomWordUseCase(wordsRepository)
    }

    @Provides
    @Singleton
    fun provideGetWordDetailsUseCase(wordsRepository: WordsRepository): GetWordDetailsUseCase {
        return GetWordDetailsUseCase(wordsRepository)
    }

    @Provides
    @Singleton
    fun provideStartWordAudioUseCase(mediaPlayer: MediaPlayer): StartWordAudioUseCase {
        return StartWordAudioUseCase(mediaPlayer)
    }

    @Provides
    @Singleton
    fun provideReadOnboardingStateUseCase(dataStoreRepository: DataStoreRepository): ReadOnboardingStateUseCase {
        return ReadOnboardingStateUseCase(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideSaveOnboardingStateUseCase(dataStoreRepository: DataStoreRepository): SaveOnboardingStateUseCase {
        return SaveOnboardingStateUseCase(dataStoreRepository)
    }
}