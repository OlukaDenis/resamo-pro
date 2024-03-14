package com.dennytech.domain.usecases.auth

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.TokenDomainModel
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
class LoginUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : BaseFlowUseCase<LoginUseCase.Param, Resource<TokenDomainModel>>(dispatcher) {

    data class Param(
        val email: String?,
        val phone: String?,
        val password: String
    )

    override fun run(param: Param?): Flow<Resource<TokenDomainModel>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val map: HashMap<String, Any> = HashMap()
            map["password"] = param.password

//            when(param.type) {
//                "email" -> map["email"] = param.email!!
//                "phone" -> {
//                    map["mobileCountryCode"] = param.countryCode!!
//                    map["mobileNumber"] = param.phone!!.trimStart('0')
//                }
//            }

            val response = runBlocking { authRepository.login(map) }

            runBlocking {profileRepository.fetchUser(HashMap())  }

            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            Timber.e(throwable)
            emit(Resource.Error(utilRepository.getNetworkError(throwable)))
        }
    }
}