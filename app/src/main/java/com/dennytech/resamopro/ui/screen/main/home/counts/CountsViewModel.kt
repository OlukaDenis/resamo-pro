package com.dennytech.resamopro.ui.screen.main.home.counts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.sales.GetSaleCountsUseCase
import com.dennytech.resamopro.ui.screen.main.home.CountCardModel
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountsViewModel @Inject constructor(
    private val getSaleCountsUseCase: GetSaleCountsUseCase
): ViewModel() {

    var state by mutableStateOf(CountsState())

    init {
        onEvent(CountsEvent.SubmitFilter)
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
    val startDate: String = ""
)

sealed class CountsEvent {
    data class StartDateChanged(val value: String): CountsEvent()
    data class EndDateChanged(val value: String): CountsEvent()
    data object SubmitFilter: CountsEvent()
}

