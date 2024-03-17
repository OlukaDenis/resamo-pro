package com.dennytech.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennytech.data.remote.datasource.ProductPagingSource
import com.dennytech.data.remote.datasource.SalePagingSource
import com.dennytech.data.remote.models.RemoteSaleCountsModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteSaleModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.SaleCountsDomainModel
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalesRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SalesRepository {
    override suspend fun fetchSales(filters: HashMap<String, Any>): Flow<PagingData<SaleDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                SalePagingSource(apiService, filters)
            }
        ).flow
    }

    override suspend fun fetchRecentSales(): List<SaleDomainModel> {
        return try {
            val request = HashMap<String, Any>().apply {
                this["page"] = 1
                this["pageSize"] = 5
            }

            val response = apiService.fetchSales(request)
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun fetchRevenue(request: HashMap<String, Any>): Int {
//        return try {
//            val response = apiService.createSale(request)
//            response.statusCode
//        } catch (throwable: Throwable) {
//            throw throwable
//        }
        return  1000
    }

    override suspend fun fetchCounts(request: HashMap<String, Any>): SaleCountsDomainModel {
        return try {
            val response = apiService.fetchSaleCounts(request)
            response.data.toDomain()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}