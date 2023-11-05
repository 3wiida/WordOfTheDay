package com.mahmoudibrahem.wordoftheday.core.di

import android.media.MediaPlayer
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mahmoudibrahem.wordoftheday.MyApplication
import com.mahmoudibrahem.wordoftheday.data.local.WordDao
import com.mahmoudibrahem.wordoftheday.data.local.WordDatabase
import com.mahmoudibrahem.wordoftheday.data.remote.RandomWordAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordAutocompleteAPI
import com.mahmoudibrahem.wordoftheday.data.remote.WordsAPI
import com.mahmoudibrahem.wordoftheday.data.repository.DataStoreRepositoryImpl
import com.mahmoudibrahem.wordoftheday.data.repository.WordsRepositoryImpl
import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import com.mahmoudibrahem.wordoftheday.domain.repository.WordsRepository
import com.mahmoudibrahem.wordoftheday.domain.usecase.GetWordsSuggestionsUseCase
import com.mahmoudibrahem.wordoftheday.domain.usecase.ReadDarkModeStateUseCase
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
        wordAutocompleteAPI: WordAutocompleteAPI,
        db: WordDatabase
    ): WordsRepository {
        return WordsRepositoryImpl(wordsAPI, randomWordAPI, wordAutocompleteAPI, db)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

}