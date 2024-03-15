package com.dennytech.domain.usecases.products

import android.net.Uri
import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.AppResource
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val productRepository: ProductRepository,
) : BaseFlowUseCase<CreateProductUseCase.Param, Resource<ProductDomainModel>>(dispatcher) {

    data class Param(
        val fileUri: Uri,
        val brand: String,
        val name: String,
        val color: String,
        val size: String,
        val type: String,
        val price: String
    )

    override fun run(param: Param?): Flow<Resource<ProductDomainModel>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, String>().apply {
                this["brand"] = param.brand
                this["name"] = param.name
                this["color"] = param.color
                this["size"] = param.size
                this["type"] = param.type
                this["price"] = param.price
            }

            val response =
                runBlocking { productRepository.createNewProduct(param.fileUri, request) }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error: ")
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}
