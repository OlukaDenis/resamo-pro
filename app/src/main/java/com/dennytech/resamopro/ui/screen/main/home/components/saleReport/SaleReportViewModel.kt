package com.dennytech.resamopro.ui.screen.main.home.components.saleReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.usecases.reports.FetchAndObserveSaleReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleReportViewModel @Inject constructor(
    private val fetchAndObserveSaleReportUseCase: FetchAndObserveSaleReportUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<SaleReportDomainModel>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<SaleReportDomainModel>>> = _uiState

    fun fetchAndObserveReport() {
        viewModelScope.launch {
            fetchAndObserveSaleReportUseCase().collect {
                _uiState.value = it
            }
        }
    }
}