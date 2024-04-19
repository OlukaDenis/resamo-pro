package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetUserStoreListUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val repository: StoreRepository
): BaseFlowUseCase<Unit, List<StoreDomainModel>>(dispatcher){
    override fun run(param: Unit?): Flow<List<StoreDomainModel>> {
        return repository.getUserStores()
    }

}
