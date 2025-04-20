package com.dennytech.resamopro.ui.screen.main.products.sale

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.sales.CreateSaleUseCase
import com.dennytech.resamopro.ui.models.events.RecordSaleEvent
import com.dennytech.resamopro.ui.models.states.RecordSaleState
import com.dennytech.resamopro.utils.Helpers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecordSaleViewModel @Inject constructor(
    private val createSaleUseCase: CreateSaleUseCase
): ViewModel() {
    var state by mutableStateOf(RecordSaleState())
    var requestComplete by mutableStateOf(false)

    @SuppressLint("NewApi")
    fun onEvent(event: RecordSaleEvent) {
        when(event) {
            is RecordSaleEvent.PriceChanged -> {
                state = state.copy(price = event.value.trim(), priceError = "", dirty = false)
            }

            is RecordSaleEvent.QuantityChanged -> {
                state = state.copy(quantity = event.value.trim(), quantityError = "", dirty = false)
            }

            is RecordSaleEvent.SaleDateChanged -> {
                val currentTimeInMillis = Helpers.getCurrentTimeInMillis()
                val millis = (event.value.toLong() + currentTimeInMillis) - 10800000 // subtract 3 hours utc difference

                state = state.copy(
                    saleDate = millis.toString(),
                    saleDateError = "",
                    saleDateText = Helpers.millisecondsToDate(millis, "dd MMM, yyyy"),
                    dirty = false
                )
            }

            is RecordSaleEvent.ToggleSaleDatePicker -> {
                state = state.copy(showSaleDatePicker = !state.showSaleDatePicker)
            }

            is RecordSaleEvent.SetProduct -> {
                state = state.copy(productId = event.value)
            }

            is RecordSaleEvent.Submit -> {
                validate()

                if (!state.dirty) {
                   submitSale()
//                    val milliseconds = state.saleDate.toLong()
//                    Timber.d("Sale date: %s", Helpers.millisecondsToDate(milliseconds, "yyyy-MM-dd'T'HH:mm:ss.SSS"))
                }
            }
        }
    }

    private fun submitSale() {
        val milliseconds = state.saleDate.toLong()
        val date = Helpers.millisecondsToDate(milliseconds, "yyyy-MM-dd'T'HH:mm:ss.SSS")

        viewModelScope.launch {

            val param = CreateSaleUseCase.Param(
                productId = state.productId,
                sellingPrice = state.price.toInt(),
                quantity = state.quantity.toInt(),
                saleDate = date
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

        if (state.saleDate.isEmpty()) {
            state = state.copy(saleDateError = "Date required", dirty = true)
        }

        if (state.quantity.isEmpty()) {
            state = state.copy(quantityError = "Quantity required", dirty = true)
        }
    }

}