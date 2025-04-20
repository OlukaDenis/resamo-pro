package com.dennytech.resamopro.ui.models.events

import android.net.Uri
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.models.KeyValueModel

sealed class CreateProductEvent {
    data class SetImageFile(val file: Uri?): CreateProductEvent()
    data class SetProductState(val value: ProductDomainModel): CreateProductEvent()
    data object ToggleSuccessDialog: CreateProductEvent()
    data class NameChanged(val value: String): CreateProductEvent()
    data class SizeChanged(val value: String): CreateProductEvent()
    data class QuantityChanged(val value: String): CreateProductEvent()
    data class ColorChanged(val value: String): CreateProductEvent()
    data class TypeChanged(val value: KeyValueModel): CreateProductEvent()
    data class CategoryChanged(val value: KeyValueModel): CreateProductEvent()
    data class BrandChanged(val value: String): CreateProductEvent()
    data class PriceChanged(val value: String): CreateProductEvent()
    data class DamagedChanged(val value: Boolean): CreateProductEvent()
    data object Submit: CreateProductEvent()
    data object Reset: CreateProductEvent()
}