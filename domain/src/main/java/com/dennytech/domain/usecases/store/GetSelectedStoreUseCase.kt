package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetSelectedStoreUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val storeRepository: StoreRepository,
    private val preferenceRepository: PreferenceRepository
): BaseFlowUseCase<Unit, StoreDomainModel>(dispatcher){
    override fun run(param: Unit?): Flow<StoreDomainModel> = flow {
        preferenceRepository.getCurrentStore().map {
            emit(storeRepository.getStoreById(it))
        }.first()
    }
}