package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveOnboardingStateUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(isOpened: Boolean) {
        dataStoreRepository.saveOnboardingOpenedState(isOpened)
    }
}