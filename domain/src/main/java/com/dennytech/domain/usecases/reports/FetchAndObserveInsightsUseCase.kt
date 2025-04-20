package com.dennytech.domain.usecases.reports

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class FetchAndObserveInsightsUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val reportsRepository: ReportsRepository
) : BaseFlowUseCase<Unit, Resource<InsightCountsDomainModel>>(dispatcher) {

    override fun run(param: Unit?): Flow<Resource<InsightCountsDomainModel>> = channelFlow {
        send(Resource.Loading)
        val defaultInsight = InsightCountsDomainModel(profit = 0L, salesCount = 0L, salesTotal = 0L)
        // Preload the cached data
        val cached = reportsRepository.getInsights().firstOrNull()
        cached?.let { send(Resource.Success(it)) }

        try {
            // Fetch remote data
            val response = reportsRepository.fetchCounts(hashMapOf())

            reportsRepository.saveInsightsToCache(response)
        } catch (throwable: Throwable) {
            send(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }

        // Observe room continuously
        reportsRepository.getInsights().collect {
            send(Resource.Success(it ?: defaultInsight))
        }
    }
}