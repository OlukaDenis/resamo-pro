package com.dennytech.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennytech.data.remote.datasource.TransactionPagingSource
import com.dennytech.data.remote.models.RemoteTransactionModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.TransactionDomainModel
import com.dennytech.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TransactionRepository {
    override suspend fun fetchTransactions(): Flow<PagingData<TransactionDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                TransactionPagingSource(apiService)
            }
        ).flow
    }

    override suspend fun fetchRecentTransactions(): List<TransactionDomainModel> {
        return try {
            val request = HashMap<String, Any>().apply {
                this["page"] = 1
                this["pageSize"] = 5
            }

            val response = apiService.getTransactions(request)
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}