package com.dennytech.domain.usecases.account

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.repository.ProfileRepository
import javax.inject.Inject

class FetchCurrentUserUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val profileRepository: ProfileRepository,
) : BaseSuspendUseCase<Unit, Unit>(dispatcher) {
    override suspend fun run(param: Unit?) {
        try {
            // Fetch current user info
            profileRepository.fetchCurrentUser()

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}