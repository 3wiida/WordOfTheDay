package com.mahmoudibrahem.wordoftheday.core.di

import android.app.Application
import androidx.room.Room
import com.mahmoudibrahem.wordoftheday.data.local.WordDao
import com.mahmoudibrahem.wordoftheday.data.local.WordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWordDatabase(app: Application): WordDatabase {
        return Room
            .databaseBuilder(app, WordDatabase::class.java, "word_db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideWordDao(db: WordDatabase): WordDao {
        return db.wordDao
    }
}