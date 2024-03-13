package com.dennytech.domain.usecases.auth

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.AppResource
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val authRepository: AuthRepository,
) : BaseFlowUseCase<SignupUseCase.Param, AppResource<String>>(dispatcher) {

    data class Param(
        val payload: HashMap<String, Any>
    )

    override fun run(param: Param?): Flow<AppResource<String>> = flow {
        emit(AppResource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val response = runBlocking { authRepository.signup(param.payload) }
            emit(AppResource.Success(response))

        } catch (throwable: Throwable) {
            emit(AppResource.Error(exception = utilRepository.getErrorBody(throwable)))
        }
    }
}