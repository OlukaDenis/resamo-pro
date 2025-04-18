package com.dennytech.domain.usecases.account

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val profileRepository: ProfileRepository
): BaseSuspendUseCase<Unit, UserDomainModel>(dispatcher){
    override suspend fun run(param: Unit?): UserDomainModel {
        return profileRepository.getCurrentUser().first() ?: throw Exception("User is null")
    }
}