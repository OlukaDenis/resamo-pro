package com.dennytech.domain.usecases.user

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class ToggleUserActivationUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val profileRepository: ProfileRepository,
) : BaseFlowUseCase<ToggleUserActivationUseCase.Param, Resource<UserDomainModel>>(dispatcher) {

    data class Param(
        val userId: String,
        val userStatus: Int
    )

    override fun run(param: Param?): Flow<Resource<UserDomainModel>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val response = if (param.userStatus == 1)
                runBlocking { profileRepository.deactivate(param.userId) }
            else
                runBlocking { profileRepository.activate(param.userId) }

            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}