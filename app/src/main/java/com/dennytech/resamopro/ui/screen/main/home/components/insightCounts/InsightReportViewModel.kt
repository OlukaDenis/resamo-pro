package com.dennytech.resamopro.ui.screen.main.home.components.insightCounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.reports.FetchAndObserveInsightsUseCase
import com.dennytech.resamopro.ui.models.CountCardModel
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightReportViewModel @Inject constructor(
    private val fetchAndObserveInsightsUseCase: FetchAndObserveInsightsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<CountCardModel>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<CountCardModel>>> = _uiState

    fun fetchAndObserveInsights() {
        viewModelScope.launch {
            fetchAndObserveInsightsUseCase().collect {
             when(it) {
                 is Resource.Loading -> _uiState.value = Resource.Loading
                 is Resource.Error -> _uiState.value = Resource.Error(it.exception)
                 is Resource.Success -> {
                     val model = it.data
                     val list = mutableListOf<CountCardModel>().apply {
                         this.add(
                             CountCardModel(
                                 "Month Sales Total",
                                 model.salesTotal.toDouble().formatCurrency()
                             )
                         )
                         this.add(
                             CountCardModel(
                                 "Month Sales Count",
                                 model.salesCount.toString()
                             )
                         )
                     }
                     _uiState.value = Resource.Success(list)
                 }
             }
            }
        }
    }
}