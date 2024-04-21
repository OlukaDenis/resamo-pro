package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.AppResource
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AssignUserToStoreUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val profileRepository: ProfileRepository,
) : BaseFlowUseCase<AssignUserToStoreUseCase.Param, AppResource<String>>(dispatcher) {

    data class Param(
        val userId: String,
        val storeId: String
    )

    override fun run(param: Param?): Flow<AppResource<String>> = flow {
        emit(AppResource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, Any>().apply {
                this["userId"] = param.userId
            }

           runBlocking { profileRepository.assignUserToStore(param.storeId, request) }

            // Fetch current user info
            profileRepository.fetchCurrentUser()

            emit(AppResource.Success("Success"))

        } catch (throwable: Throwable) {
            emit(AppResource.Error(exception = utilRepository.getErrorBody(throwable)))
        }
    }
}