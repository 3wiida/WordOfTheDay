package com.mahmoudibrahem.wordoftheday.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mahmoudibrahem.wordoftheday.core.Constants.ONBOARDING_STATE_KEY
import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {
    override suspend fun saveOnboardingOpenedState(isOpened: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[ONBOARDING_STATE_KEY] = isOpened
        }
    }

    override fun readOnboardingOpenedState(): Flow<Boolean?> {
        return dataStore.data
            .catch { cause: Throwable ->
                Log.d("``TAG``", "readOnboardingOpenedState: ${cause.message}")
            }
            .map { value: Preferences ->
                value[ONBOARDING_STATE_KEY]
            }
    }
}