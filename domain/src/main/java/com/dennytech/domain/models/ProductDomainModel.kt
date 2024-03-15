package com.dennytech.domain.models

data class ProductDomainModel(
    val brand: String,
    val categoryId: String,
    val color: String,
    val createdAt: String,
    val createdBy: String,
    val id: String,
    val image: String,
    val thumbnail: String,
    val inStock: Boolean,
    val name: String,
    val price: Int,
    val quantity: Int,
    val size: Int,
    val type: String,
    val updatedAt: String
)