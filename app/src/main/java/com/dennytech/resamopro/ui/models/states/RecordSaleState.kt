package com.dennytech.resamopro.ui.models.states

import com.dennytech.resamopro.utils.Helpers

data class RecordSaleState(
    val productId: String = "",
    val price: String = "",
    val priceError: String = "",
    val quantity: String = "1",
    val quantityError: String = "",
    val saleDate: String = System.currentTimeMillis().toString(),
    val saleDateText: String = Helpers.millisecondsToDate(System.currentTimeMillis(), "dd MMM, yyyy"),
    val saleDateError: String = "",
    val dirty: Boolean = false,
    val error: String = "",
    val loading: Boolean = false,
    val showSaleDatePicker: Boolean = false
)

