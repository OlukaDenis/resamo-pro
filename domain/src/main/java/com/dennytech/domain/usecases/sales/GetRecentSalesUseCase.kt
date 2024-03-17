package com.dennytech.domain.usecases.sales

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetRecentSalesUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val repository: SalesRepository,
) : BaseFlowUseCase<Unit, Resource<List<SaleDomainModel>>>(dispatcher) {

    override fun run(param: Unit?): Flow<Resource<List<SaleDomainModel>>> = flow {
        emit(Resource.Loading)

        try {

            val response = runBlocking { repository.fetchRecentSales() }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}