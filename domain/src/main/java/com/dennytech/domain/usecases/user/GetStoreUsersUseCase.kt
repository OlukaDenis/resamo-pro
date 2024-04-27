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

class GetStoreUsersUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val profileRepository: ProfileRepository,
) : BaseFlowUseCase<Unit, Resource<List<UserDomainModel>>>(dispatcher) {
    override fun run(param: Unit?): Flow<Resource<List<UserDomainModel>>> = flow {
        emit(Resource.Loading)

        try {
            val response = runBlocking { profileRepository.fetchStoreUserList() }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}