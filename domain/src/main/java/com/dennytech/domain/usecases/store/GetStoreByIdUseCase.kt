package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.InvalidParameterException
import javax.inject.Inject

class GetStoreByIdUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val storeRepository: StoreRepository
): BaseFlowUseCase<GetStoreByIdUseCase.Param, StoreDomainModel>(dispatcher){

    data class Param(val storeId: String)
    override fun run(param: Param?): Flow<StoreDomainModel> = flow {
        if (param == null) throw InvalidParameterException()
       emit(storeRepository.getStoreById(storeId = param.storeId))
    }
}