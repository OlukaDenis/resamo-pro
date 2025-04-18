package com.dennytech.domain.repository

import androidx.paging.PagingData
import com.dennytech.domain.models.SaleDomainModel
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    suspend fun fetchSales(filters: HashMap<String, Any>): Flow<PagingData<SaleDomainModel>>
    suspend fun fetchRecentSales(): List<SaleDomainModel>
    suspend fun fetchRevenue(request: HashMap<String, Any>): Long
    suspend fun confirmSale(saleId: String): Int
}