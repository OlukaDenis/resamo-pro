package com.dennytech.domain.usecases.transactions

import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.TransactionDomainModel
import com.dennytech.domain.repository.TransactionRepository
import com.dennytech.domain.repository.UtilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetRecentTransactionsUseCase @Inject constructor(
    private val dispatcher: AppDispatcher,
    private val utilRepository: UtilRepository,
    private val transactionRepository: TransactionRepository,
) : BaseFlowUseCase<Unit, Resource<List<TransactionDomainModel>>>(dispatcher) {

    override fun run(param: Unit?): Flow<Resource<List<TransactionDomainModel>>> = flow {
        emit(Resource.Loading)

        try {

            val response = runBlocking { transactionRepository.fetchRecentTransactions() }
            emit(Resource.Success(response))

        } catch (throwable: Throwable) {
            emit(Resource.Error(exception = utilRepository.getNetworkError(throwable)))
        }
    }
}
