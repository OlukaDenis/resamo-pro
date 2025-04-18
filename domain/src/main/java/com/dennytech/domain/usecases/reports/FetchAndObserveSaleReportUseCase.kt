package com.dennytech.domain.usecases.reports

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchRemoteSaleByPeriodUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val reportsRepository: ReportsRepository
) : BaseFlowUseCase<Unit, Resource<List<SaleReportDomainModel>>>(dispatcher) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun run(param: Unit?): Flow<Resource<List<SaleReportDomainModel>>> = channelFlow {
        send(Resource.Loading)
        // Preload the cached data
        val cached = reportsRepository.getSalesReport().first()
        send(Resource.Success(cached))

        try {
            // Fetch remote data
            val response = reportsRepository.fetchSalesByPeriod()

            // Save the new remote data to cache and emit the new data
            // This will invoke the room flow
            reportsRepository.saveSaleReportToCache(response)
//            emit(Resource.Success(response))
        } catch (throwable: Throwable) {
            send(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }

        // Observe room continuously
        reportsRepository.getSalesReport().collect {
            send(Resource.Success(it))
        }
    }
}