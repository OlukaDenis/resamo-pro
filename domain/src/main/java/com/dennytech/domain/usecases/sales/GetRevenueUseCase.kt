package com.dennytech.domain.usecases.sales

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetRevenueUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val repository: SalesRepository,
) : BaseFlowUseCase<GetRevenueUseCase.Param, Resource<Int>>(dispatcher) {

    data class Param(
        val startDate: String?,
        val endDate: String?
    )

    override fun run(param: Param?): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)

        try {
            val hashMap = HashMap<String, Any>()

            param?.let {par ->
                par.startDate?.let {
                    hashMap["startDate"] = it
                }

                par.endDate?.let {
                    hashMap["endDate"] = it
                }

            }

            val response = runBlocking { repository.fetchRevenue(hashMap) }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}