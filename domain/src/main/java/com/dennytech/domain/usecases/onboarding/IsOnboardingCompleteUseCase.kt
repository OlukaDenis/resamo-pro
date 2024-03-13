package com.dennytech.domain.usecases.onboarding

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class IsOnboardingCompleteUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val preference: PreferenceRepository
) : BaseSuspendUseCase<Unit, Boolean>(dispatcher) {
    override suspend fun run(param: Unit?): Boolean {
        return runBlocking {
            preference.isOnboardingComplete()
        }.first()
    }

}