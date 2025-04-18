package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.AppResource
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnAssignedStoreUsersUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val profileRepository: ProfileRepository,
) : BaseFlowUseCase<UnAssignedStoreUsersUseCase.Param, Resource<List<UserDomainModel>>>(dispatcher) {

    data class Param(
        val storeId: String
    )

    override fun run(param: Param?): Flow<Resource<List<UserDomainModel>>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, Any>().apply {
                this["storeId"] = param.storeId
            }

            val users = profileRepository.fetchUnassignedUsers(request) 

            emit(Resource.Success(users))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}