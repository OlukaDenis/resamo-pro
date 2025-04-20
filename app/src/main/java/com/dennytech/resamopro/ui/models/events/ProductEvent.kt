package com.dennytech.resamopro.ui.models.events

sealed class ProductEvent {
    data object GetProducts : ProductEvent()
    data object ToggleFilterDialog : ProductEvent()
    data class SelectImage(val image: String): ProductEvent()
    data object TogglePreviewDialog : ProductEvent()
    data class SetBrandFilter(val value: String) : ProductEvent()
    data class SetColorFilter(val value: String) : ProductEvent()
    data class SetTypeFilter(val value: String) : ProductEvent()
    data class SetSizeFilter(val value: String) : ProductEvent()
    data object ClearFilters : ProductEvent()
}