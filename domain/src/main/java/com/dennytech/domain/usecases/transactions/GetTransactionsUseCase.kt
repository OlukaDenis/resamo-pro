package com.dennytech.domain.usecases.transactions

import androidx.paging.PagingData
import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.TransactionDomainModel
import com.dennytech.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val repository: TransactionRepository
) : BaseSuspendUseCase<Unit, Flow<PagingData<TransactionDomainModel>>>(dispatcher) {
    override suspend fun run(param: Unit?): Flow<PagingData<TransactionDomainModel>> {
        return repository.fetchTransactions()
    }
}