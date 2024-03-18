package com.dennytech.domain.models

data class SaleDomainModel(
    val id: String,
    val product: ProductDomainModel?,
    val profit: Int,
    val sellingPrice: Int,
    val quantity: Int,
    val saleDate: String,
    val collected: Boolean
)
