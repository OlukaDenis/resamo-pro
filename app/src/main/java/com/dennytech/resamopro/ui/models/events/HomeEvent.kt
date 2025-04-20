package com.dennytech.resamopro.ui.models.events

sealed class HomeEvent {
    data object GetSales : HomeEvent()
    data object GetCurrentStore : HomeEvent()
    data object GetUserStores : HomeEvent()
    data object ToggleStoreBottomSheet: HomeEvent()
    data class SetCurrentStore(val storeId: String): HomeEvent()
}