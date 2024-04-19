package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.StoreRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NewStoreUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val storeRepository: StoreRepository,
    private val preferenceRepository: PreferenceRepository,
    private val profileRepository: ProfileRepository
) : BaseFlowUseCase<NewStoreUseCase.Param, Resource<StoreDomainModel>>(dispatcher) {

    data class Param(
        val name: String,
        val location: String,
        val description: String?,
        val category: String
    )

    override fun run(param: Param?): Flow<Resource<StoreDomainModel>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, Any>().apply {
                this["name"] = param.name
                this["location"] = param.location
                this["category"] = param.category
                param.description?.let {
                    this["description"] = it
                }
            }

            val response = runBlocking { storeRepository.createStore(request) }
            runBlocking { preferenceRepository.setCurrentStore(response.id) }

            // update current user
            profileRepository.fetchCurrentUser()

            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}