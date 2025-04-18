package com.dennytech.domain.usecases.reports

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FetchAndObserveRevenueUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val repository: SalesRepository,
    private val preferences: PreferenceRepository
) : BaseFlowUseCase<FetchAndObserveRevenueUseCase.Param, Resource<Long>>(dispatcher) {

    data class Param(
        val startDate: String?,
        val endDate: String?,
    )

    override fun run(param: Param?): Flow<Resource<Long>> = channelFlow {
        send(Resource.Loading)
        // Preload the cached data
        val cached = preferences.getRevenue().first()
        send(Resource.Success(cached))

        try {
            // Fetch remote data
            val response = repository.fetchRevenue(hashMapOf())

            preferences.setRevenue(response)
        } catch (throwable: Throwable) {
            send(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }

        // Observe room continuously
        preferences.getRevenue().collect {
            send(Resource.Success(it))
        }
    }
}