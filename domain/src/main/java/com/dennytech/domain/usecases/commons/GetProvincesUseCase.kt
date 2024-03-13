package com.dennytech.domain.usecases.commons

import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProvinceDomainModel
import com.dennytech.domain.repository.LocationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetProvincesUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val locationRepository: LocationRepository
) : BaseSuspendUseCase<Unit, List<ProvinceDomainModel>>(dispatcher) {

    override suspend fun run(param: Unit?): List<ProvinceDomainModel> {
        return runBlocking { locationRepository.getProvinces() }.first()
    }
}
