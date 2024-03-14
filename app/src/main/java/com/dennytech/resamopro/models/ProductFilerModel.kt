package com.dennytech.resamopro.models

import com.dennytech.resamopro.models.ProductFilerModel.Companion.isNoEmpty

data class ProductFilerModel(
    var color: String = "",
    var type: String = "",
    var size: String = "",
    var brand: String = ""
) {

    companion object {
        fun ProductFilerModel.isNoEmpty(): Boolean {
            return this.color.isNotEmpty() || this.type.isNotEmpty() || this.type.isNotEmpty() || this.brand.isNotEmpty()
        }
    }
}