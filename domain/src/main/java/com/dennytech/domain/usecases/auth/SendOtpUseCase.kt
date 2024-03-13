package com.dennytech.domain.usecases.auth

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val authRepository: AuthRepository,
) : BaseFlowUseCase<SendOtpUseCase.Param, Resource<Boolean>>(dispatcher) {

    data class Param(
        val email: String?,
        val phone: String?,
        val countryCode: String?,
        val type: String
    )

    override fun run(param: Param?): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val map: HashMap<String, Any> = HashMap()

            when(param.type) {
                "email" -> map["email"] = param.email!!
                "phone" -> {
                    map["mobileCountryCode"] = param.countryCode!!
                    map["mobileNumber"] = param.phone!!.trimStart('0')
                }
            }

            val response = runBlocking { authRepository.sendOtp(map) }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            Timber.d(throwable)
            emit(Resource.Error(utilRepository.getNetworkError(throwable)))
        }
    }
}