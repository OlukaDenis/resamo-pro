package com.dennytech.resamopro.ui.screen.main.home.counts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.usecases.reports.FetchPopularProductTypesUseCase
import com.dennytech.domain.usecases.reports.FetchSaleByPeriodUseCase
import com.dennytech.domain.usecases.sales.GetSaleCountsUseCase
import com.dennytech.resamopro.ui.screen.main.home.CountCardModel
import com.dennytech.resamopro.ui.screen.main.home.HomeEvent
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountsViewModel @Inject constructor(
    private val getSaleCountsUseCase: GetSaleCountsUseCase,
    private val fetchSaleByPeriodUseCase: FetchSaleByPeriodUseCase,
    private val fetchPopularProductTypesUseCase: FetchPopularProductTypesUseCase
): ViewModel() {

    var state by mutableStateOf(CountsState())

    init {
        onEvent(CountsEvent.SubmitFilter)
    }

    fun getAdminReports() {
        onEvent(CountsEvent.GetSaleByPeriod)
        onEvent(CountsEvent.GetPopularTypes)
    }

    fun onEvent(event: CountsEvent) {
        when(event) {
            is CountsEvent.EndDateChanged -> {
                state = state.copy(endDate = event.value)
            }

            is CountsEvent.StartDateChanged -> {
                state = state.copy(startDate = event.value)
            }

            is CountsEvent.SubmitFilter -> {
                if (state.endDate.isNotEmpty() && state.startDate.isNotEmpty()) {
                    val param = GetSaleCountsUseCase.Param(
                        endDate = state.endDate.ifEmpty { null },
                        startDate = state.startDate.ifEmpty { null }
                    )

                    getCounts(param)
                } else {
                    getCounts(null)
                }
            }

            is CountsEvent.GetSaleByPeriod -> getSalesReportByPeriod()

            is CountsEvent.GetPopularTypes -> getPopularTypes()
        }
    }

    private fun getPopularTypes() {
        viewModelScope.launch {
            fetchPopularProductTypesUseCase().collect {
                state = when(it) {
                    is Resource.Loading -> {
                        state.copy(loadingPopularTypes = true)
                    }
                    is Resource.Error -> {
                        state.copy(loadingPopularTypes = false)
                    }
                    is Resource.Success -> {
                        state.copy(loadingPopularTypes = false, popularTypes = it.data)
                    }
                }
            }
        }
    }

    private fun getSalesReportByPeriod() {
        viewModelScope.launch {
            fetchSaleByPeriodUseCase().collect {
                state = when(it) {
                    is Resource.Loading -> {
                        state.copy(loadingSaleByPeriod = true)
                    }
                    is Resource.Error -> {
                        state.copy(loadingSaleByPeriod = false)
                    }
                    is Resource.Success -> {
                        state.copy(loadingSaleByPeriod = false, salePeriodReport = it.data)
                    }
                }
            }
        }
    }


    private fun getCounts(param: GetSaleCountsUseCase.Param?) {
        viewModelScope.launch {
            getSaleCountsUseCase(param).collect {
                state = when (it) {
                    is Resource.Loading -> state.copy(loadingCounts = true)
                    is Resource.Success -> {
                        val model = it.data
                        val list = mutableListOf<CountCardModel>().apply {
                            this.add(
                                CountCardModel(
                                    "Month Sales Total",
                                    model.salesTotal.toDouble().formatCurrency()
                                )
                            )
                            this.add(CountCardModel("Month Sales Count", model.salesCount.toString()))
                        }

                        state.copy(
                            loadingCounts = false,
                            counts = list.toList(),
                            revenue = model.profit
                        )
                    }

                    is Resource.Error -> {
                        state.copy(
                            loadingCounts = false,
                        )
                    }
                }
            }
        }
    }

}

data class CountsState(
    val counts: List<CountCardModel> = emptyList(),
    val loadingCounts: Boolean = false,
    val revenue: Int = 0,
    val endDate: String = "",
    val startDate: String = "",
    val loadingSaleByPeriod: Boolean = false,
    val salePeriodReport: List<SaleReportDomainModel> = emptyList(),
    val loadingPopularTypes: Boolean = false,
    val popularTypes: List<ReportDomainModel> = emptyList()
)

sealed class CountsEvent {
    data class StartDateChanged(val value: String): CountsEvent()
    data class EndDateChanged(val value: String): CountsEvent()
    data object SubmitFilter: CountsEvent()
    data object GetSaleByPeriod : CountsEvent()
    data object GetPopularTypes: CountsEvent()
}

