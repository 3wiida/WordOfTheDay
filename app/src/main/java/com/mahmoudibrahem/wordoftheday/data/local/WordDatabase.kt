package com.mahmoudibrahem.wordoftheday.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahmoudibrahem.wordoftheday.data.local.entity.SuggestionEntity
import com.mahmoudibrahem.wordoftheday.data.local.entity.WordEntity

@Database(
    entities = [WordEntity::class,SuggestionEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordDao: WordDao
}