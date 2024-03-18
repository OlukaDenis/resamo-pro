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

class ConfirmSaleUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val salesRepository: SalesRepository,
) : BaseFlowUseCase<ConfirmSaleUseCase.Param, Resource<Int>>(dispatcher) {

    data class Param(
        val saleId: String,
    )

    override fun run(param: Param?): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val response =
                runBlocking { salesRepository.confirmSale(param.saleId) }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}
