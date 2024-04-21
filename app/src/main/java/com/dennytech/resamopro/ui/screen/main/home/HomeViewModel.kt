package com.dennytech.resamopro.ui.screen.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleCountsDomainModel
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.usecases.sales.GetRecentSalesUseCase
import com.dennytech.domain.usecases.sales.GetRevenueUseCase
import com.dennytech.domain.usecases.sales.GetSaleCountsUseCase
import com.dennytech.domain.usecases.store.GetSelectedStoreUseCase
import com.dennytech.domain.usecases.store.GetUserStoreListUseCase
import com.dennytech.domain.usecases.store.SetSelectedStoreUseCase
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSaleCountsUseCase: GetSaleCountsUseCase,
    private val getRevenueUseCase: GetRevenueUseCase,
    private val getRecentSalesUseCase: GetRecentSalesUseCase,
    private val getSelectedStoreUseCase: GetSelectedStoreUseCase,
    private val getUserStoreListUseCase: GetUserStoreListUseCase,
    private val setSelectedStoreUseCase: SetSelectedStoreUseCase
) : ViewModel() {

    var state by mutableStateOf(HomeState())

    init {
//        initialize()
    }

    fun initialize() {
        onEvent(HomeEvent.GetRevenue)
        onEvent(HomeEvent.GetSaleCounts)
        onEvent(HomeEvent.GetSales)
        onEvent(HomeEvent.GetCurrentStore)
        onEvent(HomeEvent.GetUserStores)
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetSaleCounts -> getCounts()

            is HomeEvent.GetRevenue -> getRevenue()

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
            runBlocking { setSelectedStoreUseCase(SetSelectedStoreUseCase.Param(storeId = storeId)) }

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

    private fun getCurrentStore() {
        viewModelScope.launch {
            getSelectedStoreUseCase().collect {
                state = state.copy(currentStore = it)
            }
        }
    }

    private fun getCounts() {
        viewModelScope.launch {
            getSaleCountsUseCase().collect {
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
                            this.add(
                                CountCardModel(
                                    "Month Sales Count",
                                    model.salesCount.toString()
                                )
                            )
                        }
                        state.copy(
                            loadingCounts = false,
                            counts = list.toList()
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

    private fun getRevenue() {
        viewModelScope.launch {
            getRevenueUseCase().collect {
                state = when (it) {
                    is Resource.Loading -> state.copy(loadingRevenue = true)
                    is Resource.Success -> {
                        state.copy(
                            loadingRevenue = false,
                            revenue = it.data
                        )
                    }

                    is Resource.Error -> {
                        state.copy(
                            loadingRevenue = false,
                        )
                    }
                }
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
    val showStoreBottomSheet: Boolean = false
)

data class CountCardModel(
    val title: String,
    val content: String
)

sealed class HomeEvent {
    data object GetSaleCounts : HomeEvent()
    data object GetRevenue : HomeEvent()
    data object GetSales : HomeEvent()
    data object GetCurrentStore : HomeEvent()
    data object GetUserStores : HomeEvent()
    data object ToggleStoreBottomSheet: HomeEvent()
    data class SetCurrentStore(val storeId: String): HomeEvent()
}