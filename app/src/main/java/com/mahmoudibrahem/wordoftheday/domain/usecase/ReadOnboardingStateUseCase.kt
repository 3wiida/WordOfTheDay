package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class ReadOnboardingStateUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<Boolean?> {
        return dataStoreRepository.readOnboardingOpenedState()
    }
}