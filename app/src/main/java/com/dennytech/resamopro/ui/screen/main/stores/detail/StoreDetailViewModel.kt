package com.dennytech.resamopro.ui.screen.main.stores.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.usecases.store.GetStoreByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreByIdUseCase: GetStoreByIdUseCase
): ViewModel() {

    var state by mutableStateOf(StoreDetailState())

    fun onEvent(event: StoreDetailEvent) {
        when(event) {
            is StoreDetailEvent.GetStore -> getStore(event.storeId)
        }
    }

    private fun getStore(storeId: String) {
        viewModelScope.launch {
            getStoreByIdUseCase(GetStoreByIdUseCase.Param(storeId = storeId)).collect {
                state = state.copy(store = it)
            }
        }
    }
}

data class StoreDetailState(
    val store: StoreDomainModel? = null
)

sealed class StoreDetailEvent {
    data class GetStore(val storeId: String): StoreDetailEvent()
}