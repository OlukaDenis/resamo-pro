package com.dennytech.domain.usecases.products

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CreateProductCategoryUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val profileRepository: ProfileRepository,
    private val productRepository: ProductRepository
) : BaseFlowUseCase<CreateProductCategoryUseCase.Param, Resource<String>>(dispatcher) {

    data class Param(
        val name: String,
        val description: String?,
        val storeId: String
    )

    override fun run(param: Param?): Flow<Resource<String>> = flow {
        emit(Resource.Loading)

        try {
            if (param == null) throw Exception("Invalid params")

            val request = HashMap<String, Any>().apply {
                this["name"] = param.name
                this["storeId"] = param.storeId
                param.description?.let {
                    this["description"] = param.description
                }
            }

            runBlocking { productRepository.createProductCategory(request) }

            // Fetch current user info
            runBlocking { profileRepository.fetchCurrentUser() }

            emit(Resource.Success("Success"))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}