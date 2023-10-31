package com.mahmoudibrahem.wordoftheday.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahmoudibrahem.wordoftheday.data.local.entity.WordEntity

@Database(
    entities = [WordEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {
    abstract val wordDao: WordDao
}