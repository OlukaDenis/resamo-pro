package com.dennytech.resamopro.ui.models.events

sealed class RecordSaleEvent {
    data class PriceChanged(val value: String): RecordSaleEvent()
    data class QuantityChanged(val value: String): RecordSaleEvent()
    data class SaleDateChanged(val value: String): RecordSaleEvent()
    data class SetProduct(val value: String): RecordSaleEvent()
    data object ToggleSaleDatePicker: RecordSaleEvent()
    data object Submit: RecordSaleEvent()
}