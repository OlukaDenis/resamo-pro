package com.dennytech.resamopro.ui.models.states

import com.dennytech.resamopro.models.ProductFilerModel

data class ProductState(
    val showFilterDialog: Boolean = false,
    val showPreviewDialog: Boolean = false,
    val selectedImage: String = "",
    val filters: ProductFilerModel = ProductFilerModel(),
    val loading: Boolean = false,
    val error: String = "",
    val empty: Boolean = false
)

