package com.mahmoudibrahem.wordoftheday.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveOnboardingOpenedState(isOpened: Boolean)

    fun readOnboardingOpenedState(): Flow<Boolean?>

    suspend fun saveDarkModeState(isDarkMode: Boolean)

    fun readDarkModeState(): Flow<Boolean?>

    suspend fun saveLatestDay(day: Int)

    fun readLatestDay(): Flow<Int?>
}