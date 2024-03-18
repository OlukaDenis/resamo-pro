package com.dennytech.domain.usecases.sales

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CreateSaleUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val productRepository: ProductRepository,
) : BaseFlowUseCase<CreateSaleUseCase.Param, Resource<Int>>(dispatcher) {

    data class Param(
        val productId: String,
        val sellingPrice: Int,
    )

    override fun run(param: Param?): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, Any>().apply {
                this["productId"] = param.productId
                this["sellingPrice"] = param.sellingPrice
                this["negotiated"] = false
            }

            val response =
                runBlocking { productRepository.createProductSale(request) }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}
