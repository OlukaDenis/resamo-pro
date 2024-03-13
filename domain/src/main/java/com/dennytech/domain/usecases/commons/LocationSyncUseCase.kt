package com.dennytech.domain.usecases.commons

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.SyncRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class LocationSyncUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val syncRepository: SyncRepository,
) : BaseFlowUseCase<Unit, Resource<String>>(dispatcher) {

    override fun run(param: Unit?): Flow<Resource<String>> = flow {
        emit(Resource.Loading)

        try {

            runBlocking { syncRepository.fetchCountries() }

            runBlocking { syncRepository.fetchProvinces() }

            runBlocking { syncRepository.fetchStates() }

            emit(Resource.Success("String"))

        } catch (throwable: Throwable) {
            Timber.e(throwable)
            emit(Resource.Error(utilRepository.getNetworkError(throwable)))
        }
    }
}