package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository

class SaveOnboardingStateUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(isOpened: Boolean) {
        dataStoreRepository.saveOnboardingOpenedState(isOpened)
    }
}