package com.dennytech.domain.usecases.commons

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.StateDomainModel
import com.dennytech.domain.repository.LocationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetStatesUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val locationRepository: LocationRepository
) : BaseSuspendUseCase<Unit, List<StateDomainModel>>(dispatcher) {

    override suspend fun run(param: Unit?): List<StateDomainModel> {
        return runBlocking { locationRepository.getStates() }.first()
    }
}