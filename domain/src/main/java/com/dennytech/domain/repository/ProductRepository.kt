package com.dennytech.domain.repository

import androidx.paging.PagingData
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.TransactionDomainModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun fetchProducts(filters: HashMap<String, Any>): Flow<PagingData<ProductDomainModel>>
    suspend fun fetchRecentProducts(): List<ProductDomainModel>
}