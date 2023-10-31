package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveCurrentDayUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(currentDay: Int) {
        dataStoreRepository.saveLatestDay(currentDay)
    }
}