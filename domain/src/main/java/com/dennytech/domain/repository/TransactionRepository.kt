package com.dennytech.domain.repository

import androidx.paging.PagingData
import com.dennytech.domain.models.TransactionDomainModel
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun fetchTransactions(): Flow<PagingData<TransactionDomainModel>>
    suspend fun fetchRecentTransactions(): List<TransactionDomainModel>
}