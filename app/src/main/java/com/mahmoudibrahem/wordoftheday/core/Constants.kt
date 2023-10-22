package com.mahmoudibrahem.wordoftheday.core

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    const val WORDS_API_BASE_URL = "https://api.dictionaryapi.dev/"
    const val RANDOM_WORD_API_BASE_URL = "https://random-word-api.herokuapp.com/"
    const val WORD_SUGGESTIONS_BASE_URL = "https://api.datamuse.com/"
    const val APP_DATA_STORE_NAME = "APP_DATA_STORE"
    val ONBOARDING_STATE_KEY = booleanPreferencesKey("ONBOARDING_OPENED_STATE_KEY")
}