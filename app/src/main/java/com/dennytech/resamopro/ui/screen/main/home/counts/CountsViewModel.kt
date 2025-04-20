package com.dennytech.resamopro.ui.screen.main.home.counts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.reports.FetchPopularProductTypesUseCase
import com.dennytech.domain.usecases.sales.GetSaleCountsUseCase
import com.dennytech.resamopro.ui.models.CountCardModel
import com.dennytech.resamopro.ui.models.events.CountsEvent
import com.dennytech.resamopro.ui.models.states.CountsState
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountsViewModel @Inject constructor(
    private val getSaleCountsUseCase: GetSaleCountsUseCase,
    private val fetchPopularProductTypesUseCase: FetchPopularProductTypesUseCase
): ViewModel() {

    var state by mutableStateOf(CountsState())

    init {
        onEvent(CountsEvent.SubmitFilter)
    }

    fun getAdminReports() {
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


