package com.dennytech.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennytech.data.remote.datasource.ProductPagingSource
import com.dennytech.data.remote.models.RemoteProductModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteTransactionModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepository {
    override suspend fun fetchProducts(filters: HashMap<String, Any>): Flow<PagingData<ProductDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                ProductPagingSource(apiService, filters)
            }
        ).flow
    }

    override suspend fun fetchRecentProducts(): List<ProductDomainModel> {
        return try {
            val request = HashMap<String, Any>().apply {
                this["page"] = 1
                this["pageSize"] = 5
            }

            val filter = HashMap<String, Any>()

            val response = apiService.getProducts(request, filter)
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}