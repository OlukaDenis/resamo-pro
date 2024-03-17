package com.dennytech.domain.models

data class SaleDomainModel(
    val product: ProductDomainModel?,
    val profit: Int,
    val sellingPrice: Int,
    val quantity: Int,
    val saleDate: String
)
