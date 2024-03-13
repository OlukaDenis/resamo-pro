package com.dennytech.domain.usecases.auth

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CheckUsernameUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val authRepository: AuthRepository,
) : BaseFlowUseCase<CheckUsernameUseCase.Param, Resource<Boolean>>(dispatcher) {

    data class Param(
        val username: String
    )

    override fun run(param: Param?): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val  payload = HashMap<String, Any>().apply {
                this["username"] = param.username
            }

            val response = runBlocking { authRepository.checkUsername(payload) }
            emit(Resource.Success(!response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}