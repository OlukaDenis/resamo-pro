package com.dennytech.domain.usecases.auth

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.TokenDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.StoreRepository
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
    private val preferenceRepository: PreferenceRepository,
    private val storeRepository: StoreRepository
) : BaseFlowUseCase<LoginUseCase.Param, Resource<UserDomainModel>>(dispatcher) {

    data class Param(
        val email: String,
        val password: String
    )

    override fun run(param: Param?): Flow<Resource<UserDomainModel>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val map: HashMap<String, Any> = HashMap()
            map["password"] = param.password
            map["email"] = param.email

            val response = runBlocking { authRepository.login(map) }

            if (response.stores.isNotEmpty()) {
                response.stores.map {
                    storeRepository.saveStore(it)
                }
            }

            if (response.isAdmin()) {
                if (response.defaultStore.isNotEmpty()) {
                    preferenceRepository.setCurrentStore(response.defaultStore)
                } else {
                    if (response.stores.isNotEmpty()) {
                        preferenceRepository.setCurrentStore(response.stores[0].id)
                    }
                }
            } else {
                if (response.stores.isNotEmpty()) {
                    preferenceRepository.setCurrentStore(response.stores[0].id)
                }
            }

//            runBlocking {profileRepository.fetchUser(HashMap())  }

            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(utilRepository.getNetworkError(throwable)))
        }
    }
}