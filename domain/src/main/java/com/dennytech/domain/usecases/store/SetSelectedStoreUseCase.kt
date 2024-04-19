package com.dennytech.domain.usecases.store

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.repository.PreferenceRepository
import javax.inject.Inject

class SetSelectedStoreUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val repository: PreferenceRepository
): BaseSuspendUseCase<SetSelectedStoreUseCase.Param, Unit>(dispatcher){

    data class Param(
        val storeId: String
    )

    override suspend fun run(param: Param?) {
       param?.let {par ->
           repository.setCurrentStore(par.storeId)
       }
    }
}
