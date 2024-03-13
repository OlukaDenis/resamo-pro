package com.dennytech.domain.usecases.onboarding

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.repository.PreferenceRepository
import javax.inject.Inject

class SetOnboardingCompleteUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val preference: PreferenceRepository
) : BaseSuspendUseCase<SetOnboardingCompleteUseCase.Param, Unit>(dispatcher) {

    data class Param(
        val value: Boolean
    )

    override suspend fun run(param: Param?) {
        param?.let {
            preference.setOnboardingComplete(it.value)
        }
    }

}