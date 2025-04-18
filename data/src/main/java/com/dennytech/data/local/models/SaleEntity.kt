package com.dennytech.data.local.models

import androidx.room.Entity
import com.dennytech.domain.models.ProductDomainModel

@Entity(tableName = "sale")
class SaleEntity(
    val id: String,
    val product: ProductDomainModel?,
    val profit: Int,
    val sellingPrice: Int,
    val quantity: Int,
    val saleDate: String,
    val collected: Boolean
)