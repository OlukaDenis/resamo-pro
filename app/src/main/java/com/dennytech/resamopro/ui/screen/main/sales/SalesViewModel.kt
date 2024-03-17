package com.dennytech.resamopro.ui.screen.main.sales

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.usecases.sales.GetSalesUseCase
import com.dennytech.resamopro.ui.screen.main.users.create.CreateUserEvent
import com.dennytech.resamopro.ui.screen.main.users.create.CreateUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val getSalesUseCase: GetSalesUseCase,
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

                if (state.endDate.isEmpty() && state.startDate.isEmpty()) {
                    state = state.copy(error = "Please provide start and end dates")
                } else {

                    val param = GetSalesUseCase.Param(
                        startDate = state.startDate.ifEmpty { null },
                        endDate = state.endDate.ifEmpty { null }
                    )

                    getSales(param)
                }
            }

            is SaleEvent.EndDateChanged -> {
                state = state.copy(endDate = event.value)
            }

            is SaleEvent.StartDateChanged -> {
                state = state.copy(startDate = event.value)
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
     data class EndDateChanged(val value: String): SaleEvent()
    data class StartDateChanged(val value: String): SaleEvent()
}