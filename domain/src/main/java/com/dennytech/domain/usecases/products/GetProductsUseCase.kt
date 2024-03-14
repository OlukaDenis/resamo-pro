package com.dennytech.domain.usecases.products

import androidx.paging.PagingData
import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import java.security.InvalidParameterException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val repository: ProductRepository
) : BaseSuspendUseCase<GetProductsUseCase.Param, Flow<PagingData<ProductDomainModel>>>(dispatcher) {

    data class Param(
        val inStock: Boolean
    )

    override suspend fun run(param: Param?): Flow<PagingData<ProductDomainModel>> {
        return param?.let { p ->
            val filter = HashMap<String, Any>().apply {
                this["inStock"] = p.inStock
            }

            repository.fetchProducts(filter)
        } ?: throw InvalidParameterException()
    }
}