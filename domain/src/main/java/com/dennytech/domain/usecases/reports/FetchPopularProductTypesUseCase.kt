package com.dennytech.domain.usecases.reports

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FetchPopularProductTypesUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val reportsRepository: ReportsRepository
) : BaseFlowUseCase<Unit, Resource<List<ReportDomainModel>>>(dispatcher) {
    override fun run(param: Unit?): Flow<Resource<List<ReportDomainModel>>> = flow{
        emit(Resource.Loading)

        try {
            val response = runBlocking { reportsRepository.fetchPopularProductTypes() }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}