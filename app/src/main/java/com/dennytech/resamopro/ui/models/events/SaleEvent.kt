package com.dennytech.resamopro.ui.models.events

sealed class SaleEvent {
    data object GetSales: SaleEvent()
    data object FilterSales: SaleEvent()
    data class ConfirmSale(val saleId: String): SaleEvent()
    data class EndDateChanged(val value: String): SaleEvent()
    data class StartDateChanged(val value: String): SaleEvent()
}