package com.dennytech.resamopro.ui.screen.main.home.components.revenue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.reports.FetchAndObserveRevenueUseCase
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RevenueViewModel @Inject constructor(
    private val fetchAndObserveRevenueUseCase: FetchAndObserveRevenueUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<Resource<String>>(Resource.Loading)
    val uiState: StateFlow<Resource<String>> = _uiState

    fun fetchAndObserveRevenue() {
        viewModelScope.launch {
            fetchAndObserveRevenueUseCase().collect {
                when(it) {
                    is Resource.Loading -> _uiState.value =  Resource.Loading
                    is Resource.Error -> _uiState.value =  Resource.Error(it.exception)
                    is Resource.Success -> _uiState.value = Resource.Success(it.data.toDouble().formatCurrency())
                }
            }
        }
    }
}