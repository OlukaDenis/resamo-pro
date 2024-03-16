package com.dennytech.domain.usecases.user

import androidx.paging.PagingData
import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val repository: ProfileRepository
) : BaseSuspendUseCase<Unit, Flow<PagingData<UserDomainModel>>>(dispatcher) {

    override suspend fun run(param: Unit?): Flow<PagingData<UserDomainModel>> {
        return   repository.fetchUserList()
    }
}
