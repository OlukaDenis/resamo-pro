package com.dennytech.domain.usecases.reports

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

//class  @Inject constructor(
//    private val dispatcher: AppDispatcher,
//    private val utilRepository: UtilRepository,
//    private val repository: SalesRepository,
//) : BaseFlowUseCase<FetchAndObserveRevenueUseCase.Param, Resource<Int>>(dispatcher) {
//
//
//
//    override fun run(param: Param?): Flow<Resource<Int>> = flow {
//        emit(Resource.Loading)
//
//        try {
//            val hashMap = HashMap<String, Any>()
//
//            param?.let {par ->
//                par.startDate?.let {
//                    hashMap["startDate"] = it
//                }
//
//                par.endDate?.let {
//                    hashMap["endDate"] = it
//                }
//
//            }
//
//            val response = runBlocking { repository.fetchRevenue(hashMap) }
//            emit(Resource.Success(response))
//
//        } catch (throwable: Throwable) {
//            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
//        }
//    }
//}


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

    override fun run(param: FetchAndObserveRevenueUseCase.Param?): Flow<Resource<Long>> = channelFlow {
        send(Resource.Loading)
        // Preload the cached data
        val cached = preferences.getRevenue().first()
        send(Resource.Success(cached))

        try {
            // Fetch remote data
            val hashMap = HashMap<String, Any>()

            param?.let {par ->
                par.startDate?.let {
                    hashMap["startDate"] = it
                }

                par.endDate?.let {
                    hashMap["endDate"] = it
                }

            }
            val response = repository.fetchRevenue(hashMap)

            preferences.setRevenue(response)
        } catch (throwable: Throwable) {
            send(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }

        // Observe room continuously
        repository.getSalesReport().collect {
            send(Resource.Success(it))
        }
    }
}