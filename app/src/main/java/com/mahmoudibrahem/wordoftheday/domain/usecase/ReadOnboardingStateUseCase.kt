package com.mahmoudibrahem.wordoftheday.domain.usecase

import com.mahmoudibrahem.wordoftheday.domain.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReadOnboardingStateUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<Boolean?> {
        return flow {
            val result=dataStoreRepository.readOnboardingOpenedState()
                .flowOn(Dispatchers.IO)
                .first()
            emit(result)
        }
    }
}