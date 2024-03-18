package com.dennytech.resamopro.ui.screen.main.sales

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennytech.data.remote.models.ConfirmSaleResponse
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.usecases.sales.ConfirmSaleUseCase
import com.dennytech.domain.usecases.sales.GetSalesUseCase
import com.dennytech.resamopro.ui.screen.main.home.CountCardModel
import com.dennytech.resamopro.ui.screen.main.users.create.CreateUserEvent
import com.dennytech.resamopro.ui.screen.main.users.create.CreateUserState
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val getSalesUseCase: GetSalesUseCase,
    private val confirmSaleUseCase: ConfirmSaleUseCase
): ViewModel() {

    init {
        onEvent(SaleEvent.GetSales)
    }

    private val _salesState: MutableStateFlow<PagingData<SaleDomainModel>> =
        MutableStateFlow(value = PagingData.empty())
    val salesState get() = _salesState
    var state by mutableStateOf(SalesState())

    fun onEvent(event: SaleEvent) {
        when(event) {
            is SaleEvent.GetSales -> {
                getSales(null)
            }

            is SaleEvent.FilterSales -> {

                if (state.endDate.isNotEmpty() && state.startDate.isNotEmpty()) {
                    val param = GetSalesUseCase.Param(
                        startDate = state.startDate.ifEmpty { null },
                        endDate = state.endDate.ifEmpty { null }
                    )

                    getSales(param)
                } else {
                    getSales(null)
                }
            }

            is SaleEvent.EndDateChanged -> {
                state = state.copy(endDate = event.value)
            }

            is SaleEvent.StartDateChanged -> {
                state = state.copy(startDate = event.value)
            }

            is SaleEvent.ConfirmSale -> {
                confirmSale(event.saleId)
            }

        }
    }

    private fun confirmSale(saleId: String) {
        viewModelScope.launch {
            confirmSaleUseCase(ConfirmSaleUseCase.Param(saleId = saleId)).collect {
                state = when (it) {
                    is Resource.Loading -> state.copy(loading = true)
                    is Resource.Success -> {
                        onEvent(SaleEvent.GetSales)
                        state.copy(
                            loading = false,
                        )
                    }

                    is Resource.Error -> {
                        state.copy(
                            loading = false,
                        )
                    }
                }
            }
        }
    }

    private fun getSales(param : GetSalesUseCase.Param?) {
        viewModelScope.launch {
            getSalesUseCase(param)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _salesState.value = it
                }
        }
    }
}

data class SalesState(
    val error: String = "",
    val loading: Boolean = false,
    val startDate: String = "",
    val endDate: String = ""
)

sealed class SaleEvent {
    data object GetSales: SaleEvent()
    data object FilterSales: SaleEvent()
    data class ConfirmSale(val saleId: String): SaleEvent()
     data class EndDateChanged(val value: String): SaleEvent()
    data class StartDateChanged(val value: String): SaleEvent()
}