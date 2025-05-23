package com.dennytech.domain.usecases

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.repository.PreferenceRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val preferenceRepository: PreferenceRepository
) : BaseSuspendUseCase<Unit, Unit>(dispatcher) {
    override suspend fun run(param: Unit?) {
        preferenceRepository.setTokenExpiry(0L)
        preferenceRepository.setCurrentStore("")
    }
}