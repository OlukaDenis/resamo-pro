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

class CreateUserUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val profileRepository: ProfileRepository,
) : BaseFlowUseCase<CreateUserUseCase.Param, Resource<UserDomainModel>>(dispatcher) {

    data class Param(
        val firstName: String,
        val lastName: String,
        val phone: String,
        val email: String,
        val password: String
    )

    override fun run(param: Param?): Flow<Resource<UserDomainModel>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, Any>().apply {
                this["firstName"] = param.firstName
                this["lastName"] = param.lastName
                this["phone"] = param.phone
                this["email"] = param.email
                this["password"] = param.password
            }

            val response =
                runBlocking { profileRepository.createUser(request) }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}