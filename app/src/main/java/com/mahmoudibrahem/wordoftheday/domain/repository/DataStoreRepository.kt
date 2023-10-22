package com.mahmoudibrahem.wordoftheday.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveOnboardingOpenedState(isOpened: Boolean)

    fun readOnboardingOpenedState(): Flow<Boolean?>
}