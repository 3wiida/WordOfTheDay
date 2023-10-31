package com.mahmoudibrahem.wordoftheday.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahmoudibrahem.wordoftheday.domain.model.Meaning
import com.mahmoudibrahem.wordoftheday.domain.model.Phonetic
import java.lang.reflect.Type

@TypeConverters
class Converters {

    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning> {
        return Gson().fromJson(
            json,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        )
    }

    @TypeConverter
    fun toMeaningsJson(obj: List<Meaning>): String {
        return Gson().toJson(
            obj,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        )
    }

    @TypeConverter
    fun fromPhoneticsJson(json: String): List<Phonetic> {
        return Gson().fromJson(
            json,
            object : TypeToken<ArrayList<Phonetic>>() {}.type
        )
    }

    @TypeConverter
    fun toPhoneticsJson(obj: List<Phonetic>): String {
        return Gson().toJson(
            obj,
            object : TypeToken<ArrayList<Phonetic>>() {}.type
        )
    }
}