package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveDarkModeStateUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(isDarkMode: Boolean) {
        dataStoreRepository.saveDarkModeState(isDarkMode)
    }
}