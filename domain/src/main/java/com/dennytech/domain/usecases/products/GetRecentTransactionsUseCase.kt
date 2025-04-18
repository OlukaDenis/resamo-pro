package com.dennytech.domain.usecases.products

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentTransactionsUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val productRepository: ProductRepository,
) : BaseFlowUseCase<Unit, Resource<List<ProductDomainModel>>>(dispatcher) {

    override fun run(param: Unit?): Flow<Resource<List<ProductDomainModel>>> = flow {
        emit(Resource.Loading)

        try {

            val response = productRepository.fetchRecentProducts()
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}
