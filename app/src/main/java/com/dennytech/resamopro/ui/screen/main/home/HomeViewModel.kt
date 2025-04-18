package com.dennytech.resamopro.ui.screen.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.usecases.account.FetchCurrentUserUseCase
import com.dennytech.domain.usecases.sales.GetRecentSalesUseCase
import com.dennytech.domain.usecases.reports.GetRevenueByPeriodUseCase
import com.dennytech.domain.usecases.sales.GetSaleCountsUseCase
import com.dennytech.domain.usecases.store.GetSelectedStoreUseCase
import com.dennytech.domain.usecases.store.GetUserStoreListUseCase
import com.dennytech.domain.usecases.store.SetSelectedStoreUseCase
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentSalesUseCase: GetRecentSalesUseCase,
    private val getSelectedStoreUseCase: GetSelectedStoreUseCase,
    private val getUserStoreListUseCase: GetUserStoreListUseCase,
    private val setSelectedStoreUseCase: SetSelectedStoreUseCase,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase
) : ViewModel() {

    var state by mutableStateOf(HomeState())

    fun initialize() {
        onEvent(HomeEvent.GetSales)
        onEvent(HomeEvent.GetUserStores)

        loadCurrentStore()
        fetchCurrentUser()
    }

    fun loadCurrentStore() {
        onEvent(HomeEvent.GetCurrentStore)
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetSales -> getRecentSales()

            is HomeEvent.GetCurrentStore -> getCurrentStore()

            is HomeEvent.ToggleStoreBottomSheet -> {
                state = state.copy(showStoreBottomSheet = !state.showStoreBottomSheet)
            }

            is HomeEvent.GetUserStores -> getUserStores()

            is HomeEvent.SetCurrentStore -> setCurrentStore(event.storeId)
        }
    }

    private fun setCurrentStore(storeId: String) {
        viewModelScope.launch {
            setSelectedStoreUseCase(SetSelectedStoreUseCase.Param(storeId = storeId))
            initialize()
        }
    }

    private fun getUserStores() {
        viewModelScope.launch {
            getUserStoreListUseCase().collect {
                state = state.copy(userStores = it)
            }
        }
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            fetchCurrentUserUseCase()
        }
    }

    private fun getCurrentStore() {
        viewModelScope.launch {
            getSelectedStoreUseCase().collect {
                state = state.copy(currentStore = it)
                Timber.d("Current store: %s", it)
            }
        }
    }

    private fun getRecentSales() {
        viewModelScope.launch {
            getRecentSalesUseCase().collect {
                state = when (it) {
                    is Resource.Loading -> state.copy(loadingSales = true)
                    is Resource.Success -> {
                        state.copy(
                            loadingSales = false,
                            sales = it.data
                        )
                    }

                    is Resource.Error -> {
                        state.copy(
                            loadingSales = false,
                        )
                    }
                }
            }
        }
    }


}

data class HomeState(
    val revenue: Int = 0,
    val counts: List<CountCardModel> = emptyList(),
    val sales: List<SaleDomainModel> = emptyList(),
    val userStores: List<StoreDomainModel> = emptyList(),
    val loadingCounts: Boolean = false,
    val loadingRevenue: Boolean = false,
    val loadingSales: Boolean = false,
    val currentStore: StoreDomainModel? = null,
    val showStoreBottomSheet: Boolean = false,
    val loadingSaleByPeriod: Boolean = false,
    val salePeriodReport: List<SaleReportDomainModel> = emptyList(),
)

data class CountCardModel(
    val title: String,
    val content: String
)

sealed class HomeEvent {
    data object GetSales : HomeEvent()
    data object GetCurrentStore : HomeEvent()
    data object GetUserStores : HomeEvent()
    data object ToggleStoreBottomSheet: HomeEvent()
    data class SetCurrentStore(val storeId: String): HomeEvent()
}