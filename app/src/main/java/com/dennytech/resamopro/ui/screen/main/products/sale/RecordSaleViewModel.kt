package com.dennytech.resamopro.ui.screen.main.products.sale

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.products.CreateSaleUseCase
import com.dennytech.resamopro.ui.screen.main.products.create.CreateProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordSaleViewModel @Inject constructor(
    private val createSaleUseCase: CreateSaleUseCase
): ViewModel() {
    var state by mutableStateOf(RecordSaleState())
    var requestComplete by mutableStateOf(false)

    fun onEvent(event: RecordSaleEvent) {
        when(event) {
            is RecordSaleEvent.PriceChanged -> {
                state = state.copy(price = event.value.trim(), priceError = "", dirty = false)
            }

            is RecordSaleEvent.SetProduct -> {
                state = state.copy(productId = event.value)
            }

            is RecordSaleEvent.Submit -> {
                validate()

                if (!state.dirty) {
                   submitSale()
                }
            }
        }
    }

    private fun submitSale() {
        viewModelScope.launch {

            val param = CreateSaleUseCase.Param(
                productId = state.productId,
                sellingPrice = state.price.toInt()
            )

            createSaleUseCase(param).collect {
                when(it) {
                    is Resource.Loading -> state = state.copy(loading = true)
                    is Resource.Success -> {
                        state = state.copy(
                            loading = false,
                            error = ""
                        )
                        requestComplete = true
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            loading = false,
                            error = it.exception
                        )
                        requestComplete = false
                    }
                }
            }
        }
    }

    private fun validate() {
        if (state.price.isEmpty()) {
            state = state.copy(priceError = "Field required", dirty = true)
        }
    }

}

data class RecordSaleState(
    val productId: String = "",
    val price: String = "",
    val priceError: String = "",
    val dirty: Boolean = false,
    val error: String = "",
    val loading: Boolean = false
)

sealed class RecordSaleEvent {
    data class PriceChanged(val value: String): RecordSaleEvent()
    data class SetProduct(val value: String): RecordSaleEvent()
    data object Submit: RecordSaleEvent()
}