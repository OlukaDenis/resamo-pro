package com.dennytech.domain.models

data class SaleDomainModel(
    val id: String = "",
    val product: ProductDomainModel? = null,
    val profit: Int = -1,
    val sellingPrice: Int = -1,
    val quantity: Int = -1,
    val saleDate: String = "",
    val collected: Boolean = false
)
