package com.dennytech.domain.usecases.sales

import androidx.paging.PagingData
import com.dennytech.domain.base.BaseSuspendUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSalesUseCase @Inject constructor(
    dispatcher: AppDispatcher,
    private val repository: SalesRepository
) : BaseSuspendUseCase<GetSalesUseCase.Param, Flow<PagingData<SaleDomainModel>>>(dispatcher) {

    data class Param(
        val startDate: String?,
        val endDate: String?
    )

    override suspend fun run(param: Param?): Flow<PagingData<SaleDomainModel>> {
        val hashMap = HashMap<String, Any>()

        param?.let {par ->
            par.startDate?.let {
                hashMap["startDate"] = it
            }

            par.endDate?.let {
                hashMap["endDate"] = it
            }
        }

        return repository.fetchSales(hashMap)
    }
}
